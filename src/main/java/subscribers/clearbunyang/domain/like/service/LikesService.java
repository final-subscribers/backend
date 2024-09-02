package subscribers.clearbunyang.domain.like.service;


import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired private RedisTemplate<String, Object> redisTemplate;

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

        String key = memberId + ":" + propertyId;

        Boolean isLiked = (Boolean) redisTemplate.opsForHash().get("likes", key);

        System.out.println("Current like status for key " + key + ": " + isLiked);

        if (Boolean.TRUE.equals(isLiked)) {
            redisTemplate.opsForHash().put("likes", key, false);
            System.out.println("Removed like for key " + key);
        } else {
            redisTemplate.opsForHash().put("likes", key, true);
            System.out.println("Added like for key " + key);
        }
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
