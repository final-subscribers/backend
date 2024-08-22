package subscribers.clearbunyang.domain.auth.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.global.exception.Invalid.InvalidValueException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;
import subscribers.clearbunyang.global.sms.model.SmsCertificationDao;
import subscribers.clearbunyang.global.sms.model.SmsCertificationRequest;
import subscribers.clearbunyang.global.sms.service.SmsCertificationUtil;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthSmsService {

    private final SmsCertificationUtil smsCertificationUtil;
    private final SmsCertificationDao smsCertificationDao;

    @Transactional
    public void sendVerificationCode(SmsCertificationRequest request) {
        String to = request.getPhone();
        int randomNumber = (int) (Math.random() * 9000) + 1000;
        String certificationNumber = String.valueOf(randomNumber);
        smsCertificationUtil.sendSms(to, certificationNumber);
        smsCertificationDao.createSmsCertification(to, certificationNumber);
    }

    @Transactional
    public void verifyCode(SmsCertificationRequest request) {
        if (!isVerify(request)) {
            throw new InvalidValueException(ErrorCode.INVALID_VERIFICATION_CODE);
        }
        smsCertificationDao.removeSmsCertification(request.getPhone());
    }

    @Transactional
    public boolean isVerify(SmsCertificationRequest request) {
        return (smsCertificationDao.hasKey(request.getPhone())
                && smsCertificationDao
                        .getSmsCertification(request.getPhone())
                        .equals(request.getCertificationNumber()));
    }
}
