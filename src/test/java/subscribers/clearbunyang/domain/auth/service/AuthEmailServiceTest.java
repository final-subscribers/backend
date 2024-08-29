package subscribers.clearbunyang.domain.auth.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import subscribers.clearbunyang.domain.user.model.request.AdminSignUpRequest;
import subscribers.clearbunyang.domain.user.repository.AdminRepository;
import subscribers.clearbunyang.global.email.service.EmailService;
import subscribers.clearbunyang.global.exception.Invalid.InvalidValueException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;

@ExtendWith(MockitoExtension.class)
class AuthEmailServiceTest {

    @Mock private EmailService emailService;

    @Mock private AdminRepository adminRepository;

    @Mock private RedisTemplate<String, Object> redisTemplate;

    @Mock private ValueOperations<String, Object> valueOperations;

    @InjectMocks private AuthEmailService authEmailService;

    @BeforeEach
    void setUp() {
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void sendVerificationCode_ShouldThrowException_WhenEmailIsDuplicated() {
        // 중복된 이메일 설정
        String email = "test@example.com";
        when(adminRepository.existsByEmail(email)).thenReturn(true);

        InvalidValueException exception =
                assertThrows(
                        InvalidValueException.class,
                        () -> {
                            authEmailService.sendVerificationCode(email);
                        });

        assertEquals(ErrorCode.EMAIL_DUPLICATION.getMessage(), exception.getMessage());

        verify(adminRepository).existsByEmail(email);
    }

    @Test
    void sendVerificationCode_ShouldSendCode_WhenEmailIsNotDuplicated() {
        // 이메일 중복되지 않는 경우
        String email = "test@example.com";
        when(adminRepository.existsByEmail(email)).thenReturn(false);
        doNothing().when(emailService).sendVerifyEmail(anyString(), anyString(), anyString());

        authEmailService.sendVerificationCode(email);

        verify(adminRepository).existsByEmail(email);
        verify(valueOperations)
                .set(eq("email:verification:" + email), anyString(), eq(Duration.ofMinutes(5)));
        verify(emailService).sendVerifyEmail(eq(email), eq("시행사 회원가입 인증코드입니다"), anyString());
    }

    @Test
    void verifyCode_ShouldThrowException_WhenCodeDoesNotMatch() {
        // 인증코드 일치하지 않는 경우
        String email = "test@example.com";
        String code = "123456";
        when(valueOperations.get("email:verification:" + email)).thenReturn("654321");

        assertThatThrownBy(() -> authEmailService.verifyCode(email, code))
                .isInstanceOf(InvalidValueException.class)
                .hasMessageContaining(ErrorCode.INVALID_VERIFICATION_CODE.getMessage());

        verify(valueOperations).get("email:verification:" + email);
        verify(redisTemplate, never()).delete(anyString());
        verify(valueOperations, never()).set(eq("email:verified:" + email), eq(true));
    }

    @Test
    void verifyCode_ShouldVerify_WhenCodeMatches() {
        // 인증코드 일치하는 경우
        String email = "test@example.com";
        String code = "123456";
        when(valueOperations.get("email:verification:" + email)).thenReturn(code);

        authEmailService.verifyCode(email, code);

        verify(valueOperations).get("email:verification:" + email);
        verify(redisTemplate).delete("email:verification:" + email);
        verify(valueOperations).set("email:verified:" + email, true);
    }

    @Test
    void isVerified_ShouldThrowException_WhenEmailNotVerified() {
        // 이메일 인증되지 않은 경우
        String email = "test@example.com";
        AdminSignUpRequest request = AdminSignUpRequest.builder().email(email).build();
        when(valueOperations.get("email:verified:" + email)).thenReturn(null);

        assertThatThrownBy(() -> authEmailService.isVerified(request))
                .isInstanceOf(InvalidValueException.class)
                .hasMessageContaining(ErrorCode.INVALID_VERIFICATION_EMAIL.getMessage());

        verify(valueOperations).get("email:verified:" + email);
    }

    @Test
    void isVerified_ShouldPass_WhenEmailIsVerified() {
        // 이메일 인증된 경우
        String email = "test@example.com";
        AdminSignUpRequest request = AdminSignUpRequest.builder().email(email).build();
        when(valueOperations.get("email:verified:" + email)).thenReturn(true);

        authEmailService.isVerified(request);

        verify(valueOperations).get("email:verified:" + email);
    }
}
