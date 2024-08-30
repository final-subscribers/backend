package subscribers.clearbunyang.global.sms.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SmsCertificationCodeRequest {

    private String phoneNumber;
    private String certificationCode;
}
