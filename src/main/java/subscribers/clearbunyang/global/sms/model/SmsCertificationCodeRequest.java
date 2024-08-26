package subscribers.clearbunyang.global.sms.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SmsCertificationCodeRequest {

    private String phoneNumber;
    private String certificationCode;
}
