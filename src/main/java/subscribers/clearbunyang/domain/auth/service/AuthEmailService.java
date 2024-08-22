package subscribers.clearbunyang.domain.auth.service;


import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.domain.user.model.request.AdminSignUpRequest;
import subscribers.clearbunyang.domain.user.repository.AdminRepository;
import subscribers.clearbunyang.global.email.service.EmailService;
import subscribers.clearbunyang.global.exception.Invalid.InvalidValueException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthEmailService {

    private final EmailService emailService;
    private final AdminRepository adminRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String EMAIL_VERIFICATION_PREFIX = "email:verification:";
    private static final String EMAIL_VERIFIED_PREFIX = "email:verified:";

    @Transactional
    public void sendVerificationCode(String email) {
        if (adminRepository.existsByEmail(email)) {
            throw new InvalidValueException(ErrorCode.EMAIL_DUPLICATION);
        }

        String verificationCode = generateVerificationCode();
        redisTemplate
                .opsForValue()
                .set(EMAIL_VERIFICATION_PREFIX + email, verificationCode, Duration.ofMinutes(5));
        emailService.sendVerifyEmail(email, "시공사 회원가입 인증코드입니다", "인증코드: " + verificationCode);
    }

    @Transactional
    public void verifyCode(String email, String code) {
        String key = EMAIL_VERIFICATION_PREFIX + email;
        String storedCode = (String) redisTemplate.opsForValue().get(key);

        if (storedCode == null || !storedCode.equals(code)) {
            throw new InvalidValueException(ErrorCode.INVALID_VERIFICATION_CODE);
        }

        redisTemplate.delete(key);
        redisTemplate.opsForValue().set(EMAIL_VERIFIED_PREFIX + email, true);
    }

    private String generateVerificationCode() {
        return RandomStringUtils.randomAlphanumeric(6);
    }

    @Transactional
    public void isVerified(AdminSignUpRequest request) {
        Boolean isVerified =
                (Boolean)
                        redisTemplate.opsForValue().get(EMAIL_VERIFIED_PREFIX + request.getEmail());

        if (isVerified == null || !isVerified) {
            throw new IllegalStateException(
                    "Email verification status not found for: " + request.getEmail());
        }
    }
}
