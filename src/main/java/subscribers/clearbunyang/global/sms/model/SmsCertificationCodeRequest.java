package subscribers.clearbunyang.global.sms.model;


import lombok.Getter;

@Getter
public class SmsCertificationCodeRequest {

    private String phoneNumber;
    private String certificationCode;
}
