package subscribers.clearbunyang.domain.likes.service;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.domain.likes.model.response.LikesPropertyResponse;
import subscribers.clearbunyang.domain.likes.repository.LikesRepository;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.repository.PropertyRepository;
import subscribers.clearbunyang.domain.user.entity.Member;
import subscribers.clearbunyang.domain.user.repository.MemberRepository;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;
import subscribers.clearbunyang.global.exception.notFound.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
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

        // DB에서 좋아요가 이미 존재하는지 확인
        boolean isLikedInDb = likesRepository.existsByMemberIdAndPropertyId(memberId, propertyId);

        // Redis에서 현재 좋아요 상태를 확인
        Boolean isLikedInRedis = (Boolean) redisTemplate.opsForHash().get("likes", key);

        System.out.println("Current like status for key " + key + ": " + isLikedInRedis);

        if (isLikedInRedis == null) {
            // Redis에 정보가 없을 경우, DB 상태에 따라 초기 값을 설정
            redisTemplate.opsForHash().put("likes", key, !isLikedInDb);
            System.out.println("Set initial like status for key " + key + " to " + !isLikedInDb);
        } else {
            // Redis에 정보가 있는 경우, 반전된 값으로 설정
            redisTemplate.opsForHash().put("likes", key, !isLikedInRedis);
            System.out.println("Toggled like status for key " + key + " to " + !isLikedInRedis);
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

        // 상태 open인지 closed인지에 따라 날짜에 해당하는 페이징된 물건값 싹 다 받아와서 필터 통과시키기
        if (status.equalsIgnoreCase("open")) {
            properties = propertyRepository.findByDateRange(currentDate, pageRequest, true);
        } else {
            properties = propertyRepository.findByDateRange(currentDate, pageRequest, false);
        }

        List<Property> filteredProperties =
                properties.stream()
                        .filter(
                                property -> {
                                    String key = memberId + ":" + property.getId();
                                    Boolean isLikedInRedis =
                                            (Boolean) redisTemplate.opsForHash().get("likes", key);

                                    // Redis에서 키값 true인경우 물건값 반환되어야함
                                    if (Boolean.TRUE.equals(isLikedInRedis)) {
                                        return true;
                                    }

                                    // Redis에서 키값 false인경우 물건값 반환되면 안됨
                                    if (Boolean.FALSE.equals(isLikedInRedis)) {
                                        return false;
                                    }

                                    // Redis에서 키값 없으면 좋아요 유무 db에서 확인해서 반환여부 결정
                                    return likesRepository.existsByMemberIdAndPropertyId(
                                            memberId, property.getId());
                                })
                        .collect(Collectors.toList());

        return new PageImpl<>(filteredProperties, pageRequest, filteredProperties.size())
                .map(LikesPropertyResponse::fromEntity);
    }
}
