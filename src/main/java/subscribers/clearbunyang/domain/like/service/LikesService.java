package subscribers.clearbunyang.domain.like.service;


import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.domain.like.entity.Likes;
import subscribers.clearbunyang.domain.like.model.response.LikesPropertyResponse;
import subscribers.clearbunyang.domain.like.repository.LikesRepository;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.repository.PropertyRepository;
import subscribers.clearbunyang.domain.user.entity.Member;
import subscribers.clearbunyang.domain.user.repository.MemberRepository;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;
import subscribers.clearbunyang.global.exception.notFound.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final MemberRepository memberRepository;
    private final PropertyRepository propertyRepository;

    @Transactional
    public void toggleLike(Long memberId, Long propertyId) {
        Member member =
                memberRepository
                        .findById(memberId)
                        .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        Property property =
                propertyRepository
                        .findById(propertyId)
                        .orElseThrow(
                                () -> new EntityNotFoundException(ErrorCode.PROPERTY_NOT_FOUND));

        Optional<Likes> existingLike = likesRepository.findByMemberAndProperty(member, property);

        if (existingLike.isPresent()) {
            likesRepository.delete(existingLike.get());
            property.setLikeCount(property.getLikeCount() - 1);
        } else {
            Likes like = Likes.builder().member(member).property(property).build();
            likesRepository.save(like);
            property.setLikeCount(property.getLikeCount() + 1);
        }

        propertyRepository.save(property);
    }

    @Transactional(readOnly = true)
    public Page<LikesPropertyResponse> getMyFavoriteProperties(
            Long memberId, String status, int page, int size) {
        Member member =
                memberRepository
                        .findById(memberId)
                        .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        LocalDate currentDate = LocalDate.now();

        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Property> properties;

        if (status.equalsIgnoreCase("open")) {
            properties =
                    propertyRepository.findAllByMemberAndDateRange(
                            member, currentDate, pageRequest, true);
        } else {
            properties =
                    propertyRepository.findAllByMemberAndDateRange(
                            member, currentDate, pageRequest, false);
        }

        return properties.map(LikesPropertyResponse::fromEntity);
    }
}
