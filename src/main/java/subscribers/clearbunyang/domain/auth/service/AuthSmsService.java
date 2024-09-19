package subscribers.clearbunyang.domain.auth.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.domain.auth.dto.request.MemberSignUpRequest;
import subscribers.clearbunyang.domain.auth.dto.request.SmsCertificationCodeRequest;
import subscribers.clearbunyang.domain.auth.repository.MemberRepository;
import subscribers.clearbunyang.global.exception.InvalidValueException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthSmsService {

    private final SendSmsService sendSmsService;
    private final SendSmsDao sendSmsDao;
    private final MemberRepository memberRepository;

    @Transactional
    public void sendVerificationCode(String phoneNumber) {
        log.info("전화번호: {}", phoneNumber);

        if (memberRepository.existsByPhoneNumber(phoneNumber)) {
            throw new InvalidValueException(ErrorCode.PHONE_DUPLICATION);
        }

        int randomNumber = (int) (Math.random() * 9000) + 1000;
        String certificationNumber = String.valueOf(randomNumber);
        sendSmsService.sendSms(phoneNumber, certificationNumber);
        sendSmsDao.createSmsCertification(phoneNumber, certificationNumber);
    }

    @Transactional
    public void verifyCode(SmsCertificationCodeRequest request) {
        if (!isVerify(request)) {
            throw new InvalidValueException(ErrorCode.INVALID_VERIFICATION_CODE);
        }
        sendSmsDao.markAsVerified(request.getPhoneNumber());
        sendSmsDao.removeSmsCertification(request.getPhoneNumber());
    }

    @Transactional
    public boolean isVerify(SmsCertificationCodeRequest request) {
        return (sendSmsDao.hasKey(request.getPhoneNumber())
                && sendSmsDao
                        .getSmsCertification(request.getPhoneNumber())
                        .equals(request.getCertificationCode()));
    }

    @Transactional
    public void isVerified(MemberSignUpRequest request) {
        if (!sendSmsDao.isVerified(request.getPhoneNumber())) {
            throw new InvalidValueException(ErrorCode.INVALID_VERIFICATION_SMS);
        }
    }
}
