package subscribers.clearbunyang.global.config;


import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.domain.like.entity.Likes;
import subscribers.clearbunyang.domain.like.repository.LikesRepository;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.repository.PropertyRepository;
import subscribers.clearbunyang.domain.user.entity.Member;
import subscribers.clearbunyang.domain.user.repository.MemberRepository;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;
import subscribers.clearbunyang.global.exception.notFound.EntityNotFoundException;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class BatchConfig {

    private final RedisTemplate<String, Object> redisTemplate;
    private final LikesRepository likesRepository;
    private final MemberRepository memberRepository;
    private final PropertyRepository propertyRepository;

    @Scheduled(cron = "0 0/27 * * * *") // 매 정각에 실행
    @Async("likesExecutor")
    @Transactional
    public void syncLikesToDatabase() {
        Map<Object, Object> likesMap = redisTemplate.opsForHash().entries("likes");

        for (Map.Entry<Object, Object> entry : likesMap.entrySet()) {
            String key = (String) entry.getKey();
            Boolean isLikedInRedis = (Boolean) entry.getValue();

            String[] parts = key.split(":");
            Long memberId = Long.parseLong(parts[0]);
            Long propertyId = Long.parseLong(parts[1]);

            Member member =
                    memberRepository
                            .findById(memberId)
                            .orElseThrow(
                                    () -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

            Property property =
                    propertyRepository
                            .findById(propertyId)
                            .orElseThrow(
                                    () ->
                                            new EntityNotFoundException(
                                                    ErrorCode.PROPERTY_NOT_FOUND));

            Optional<Likes> existingLike =
                    likesRepository.findByMemberAndProperty(member, property);

            if (Boolean.TRUE.equals(isLikedInRedis)) {
                if (existingLike.isPresent()) {
                    // DB에 좋아요가 존재하면 삭제
                    likesRepository.delete(existingLike.get());
                    property.setLikeCount(property.getLikeCount() - 1);
                    System.out.println("Removed like for key " + key);
                } else {
                    // DB에 좋아요가 없으면 추가
                    Likes like = Likes.builder().member(member).property(property).build();
                    likesRepository.save(like);
                    property.setLikeCount(property.getLikeCount() + 1);
                    System.out.println("Added like for key " + key);
                }
            } else {
                // Redis 상태가 false일 때는 아무 작업도 하지 않음
                System.out.println("No action needed for key " + key);
            }

            // DB에 변경 사항 저장
            propertyRepository.save(property);
        }

        // 캐시 초기화
        redisTemplate.opsForHash().delete("likes", likesMap.keySet().toArray());
    }
}
