package subscribers.clearbunyang.global.sms.service;


import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class SmsCertificationDao {

    private final String SMS_VERIFIED_PREFIX = "sms: ";
    private final int LIMIT_TIME = 3 * 60;

    private final StringRedisTemplate redisTemplate;

    public void createSmsCertification(String phone, String certificationNumber) {
        redisTemplate
                .opsForValue()
                .set(
                        SMS_VERIFIED_PREFIX + phone,
                        certificationNumber,
                        Duration.ofSeconds(LIMIT_TIME));
    }

    public String getSmsCertification(String phone) {
        return redisTemplate.opsForValue().get(SMS_VERIFIED_PREFIX + phone);
    }

    public void markAsVerified(String phone) {
        redisTemplate
                .opsForValue()
                .set(SMS_VERIFIED_PREFIX + phone + ":verified", "true", Duration.ofMinutes(30));
    }

    public void removeSmsCertification(String phone) {
        redisTemplate.delete(SMS_VERIFIED_PREFIX + phone);
    }

    public boolean hasKey(String phone) {
        return redisTemplate.hasKey(SMS_VERIFIED_PREFIX + phone);
    }

    public boolean isVerified(String phone) {
        String isVerified =
                redisTemplate.opsForValue().get(SMS_VERIFIED_PREFIX + phone + ":verified");
        return "true".equals(isVerified);
    }
}
