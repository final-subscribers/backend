package subscribers.clearbunyang.domain.auth.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.domain.user.model.request.MemberSignUpRequest;
import subscribers.clearbunyang.domain.user.repository.MemberRepository;
import subscribers.clearbunyang.global.exception.Invalid.InvalidValueException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;
import subscribers.clearbunyang.global.sms.model.SmsCertificationCodeRequest;
import subscribers.clearbunyang.global.sms.service.SmsCertificationDao;
import subscribers.clearbunyang.global.sms.service.SmsCertificationUtil;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthSmsService {

    private final SmsCertificationUtil smsCertificationUtil;
    private final SmsCertificationDao smsCertificationDao;
    private final MemberRepository memberRepository;

    @Transactional
    public void sendVerificationCode(String phoneNumber) {
        log.info("전화번호: {}", phoneNumber);

        if (memberRepository.existsByPhoneNumber(phoneNumber)) {
            throw new InvalidValueException(ErrorCode.PHONE_DUPLICATION);
        }

        int randomNumber = (int) (Math.random() * 9000) + 1000;
        String certificationNumber = String.valueOf(randomNumber);
        smsCertificationUtil.sendSms(phoneNumber, certificationNumber);
        smsCertificationDao.createSmsCertification(phoneNumber, certificationNumber);
    }

    @Transactional
    public void verifyCode(SmsCertificationCodeRequest request) {
        if (!isVerify(request)) {
            throw new InvalidValueException(ErrorCode.INVALID_VERIFICATION_CODE);
        }
        smsCertificationDao.markAsVerified(request.getPhoneNumber());
        smsCertificationDao.removeSmsCertification(request.getPhoneNumber());
    }

    @Transactional
    public boolean isVerify(SmsCertificationCodeRequest request) {
        return (smsCertificationDao.hasKey(request.getPhoneNumber())
                && smsCertificationDao
                        .getSmsCertification(request.getPhoneNumber())
                        .equals(request.getCertificationCode()));
    }

    @Transactional
    public void isVerified(MemberSignUpRequest request) {
        if (!smsCertificationDao.isVerified(request.getPhoneNumber())) {
            throw new InvalidValueException(ErrorCode.INVALID_VERIFICATION_SMS);
        }
    }
}
