package subscribers.clearbunyang.domain.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import subscribers.clearbunyang.domain.user.model.request.MemberSignUpRequest;
import subscribers.clearbunyang.domain.user.repository.MemberRepository;
import subscribers.clearbunyang.global.exception.Invalid.InvalidValueException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;
import subscribers.clearbunyang.global.sms.model.SmsCertificationCodeRequest;
import subscribers.clearbunyang.global.sms.model.SmsCertificationDao;
import subscribers.clearbunyang.global.sms.service.SmsCertificationUtil;

@ExtendWith(MockitoExtension.class)
public class AuthSmsServiceTest {

    @Mock
    private SmsCertificationUtil smsCertificationUtil;

    @Mock
    private SmsCertificationDao smsCertificationDao;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private AuthSmsService authSmsService;

    @BeforeEach
    void setUp() {
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    // 전화번호 중복되지 않았을 때
    @Test
    void sendVerificationCode_ShouldSendVerificationCode_WhenPhoneNumberIsNotDuplicated() {
        String phoneNumber = "01012345678";
        when(memberRepository.existsByPhoneNumber(phoneNumber)).thenReturn(false);
        doNothing().when(smsCertificationUtil).sendSms(anyString(), anyString());
        doNothing().when(smsCertificationDao).createSmsCertification(anyString(), anyString());

        authSmsService.sendVerificationCode(phoneNumber);

        verify(smsCertificationUtil, times(1)).sendSms(anyString(), anyString());
        verify(smsCertificationDao, times(1)).createSmsCertification(anyString(), anyString());
    }

    // 전화번호 중복일 때
    @Test
    void sendVerificationCode_ShouldThrowException_WhenPhoneNumberIsDuplicated() {
        String phoneNumber = "01012345678";
        when(memberRepository.existsByPhoneNumber(phoneNumber)).thenReturn(true);

        InvalidValueException exception = assertThrows(InvalidValueException.class, () -> authSmsService.sendVerificationCode(phoneNumber));

       assertEquals(ErrorCode.PHONE_DUPLICATION.getMessage(), exception.getMessage());
       verify(smsCertificationUtil, never()).sendSms(anyString(), anyString());
    }

    // 인증번호가 올바른 경우
    @Test
    void verifyCode_ShouldVerify_WhenCertificationCodeIsCorrect() {
        SmsCertificationCodeRequest request = new SmsCertificationCodeRequest("01012345678", "1234");
        when(smsCertificationDao.hasKey(request.getPhoneNumber())).thenReturn(true);
        when(smsCertificationDao.getSmsCertification(request.getPhoneNumber())).thenReturn("1234");

        authSmsService.verifyCode(request);

        verify(smsCertificationDao, times(1)).markAsVerified(request.getPhoneNumber());
        verify(smsCertificationDao, times(1)).removeSmsCertification(request.getPhoneNumber());
    }

    // 인증번호가 올바르지 않는 경우
    @Test
    void verifyCode_ShouldThrowException_WhenCertificationCodeIsInvalid() {
        SmsCertificationCodeRequest request = new SmsCertificationCodeRequest("01012345678", "1234");
        when(smsCertificationDao.hasKey(request.getPhoneNumber())).thenReturn(true);
        when(smsCertificationDao.getSmsCertification(request.getPhoneNumber())).thenReturn("5678");

        InvalidValueException exception = assertThrows(InvalidValueException.class, () -> authSmsService.verifyCode(request));

        assertEquals(ErrorCode.INVALID_VERIFICATION_CODE.getMessage(), exception.getMessage());
        verify(smsCertificationDao, never()).markAsVerified(request.getPhoneNumber());
        verify(smsCertificationDao, never()).removeSmsCertification(request.getPhoneNumber());
    }

    // isVerify 성공
    @Test
    void isVerify_ShouldReturnTrue_WhenCertificationCodeIsCorrect() {
        SmsCertificationCodeRequest request = new SmsCertificationCodeRequest("01012345678", "1234");
        when(smsCertificationDao.hasKey(request.getPhoneNumber())).thenReturn(true);
        when(smsCertificationDao.getSmsCertification(request.getPhoneNumber())).thenReturn("1234");

        boolean result = authSmsService.isVerify(request);

        assertTrue(result, "인증번호가 일치하여 인증되었습니다.");
    }

    // isVerify에서 인증번호가 없을 경우
    @Test
    void isVerify_ShouldReturnFalse_WhenCertificationCodeDoesNotExist() {
        SmsCertificationCodeRequest request = new SmsCertificationCodeRequest("01012345678", "1234");
        when(smsCertificationDao.hasKey(request.getPhoneNumber())).thenReturn(false);

        boolean result = authSmsService.isVerify(request);

        assertFalse(result, "인증번호가 없습니다.");
    }

    // isVerify에서 인증번호가 일치하지 않을 경우
    @Test
    void isVerify_ShouldReturnFalse_WhenCertificationCodeIsIncorrect() {
        SmsCertificationCodeRequest request = new SmsCertificationCodeRequest("01012345678", "1234");
        when(smsCertificationDao.hasKey(request.getPhoneNumber())).thenReturn(true);
        when(smsCertificationDao.getSmsCertification(request.getPhoneNumber())).thenReturn("5678");

        boolean result = authSmsService.isVerify(request);

        assertFalse(result, "인증번호가 일치하지 않습니다.");
    }

    // 인증받았던 경우
    @Test
    void isVerified_ShouldDoNothing_WhenPhoneNumberIsVerified() {
        MemberSignUpRequest request = MemberSignUpRequest.builder()
            .phoneNumber("01012345678")
            .build()
            ;
        when(smsCertificationDao.isVerified(request.getPhoneNumber())).thenReturn(true);

        authSmsService.isVerified(request);

        verify(smsCertificationDao, times(1)).isVerified(request.getPhoneNumber());
    }

    // 인증 안받았던 경우
    @Test
    void isVerified_ShouldThrowException_WhenPhoneNumberIsNotVerified() {
        MemberSignUpRequest request = MemberSignUpRequest.builder()
            .phoneNumber("01012345678")
            .build()
            ;
        when(smsCertificationDao.isVerified(request.getPhoneNumber())).thenReturn(false);

        InvalidValueException exception = assertThrows(InvalidValueException.class, () -> authSmsService.isVerified(request));

        assertEquals(ErrorCode.INVALID_VERIFICATION_SMS.getMessage(), exception.getMessage());
        verify(smsCertificationDao, times(1)).isVerified(request.getPhoneNumber());
    }
}
