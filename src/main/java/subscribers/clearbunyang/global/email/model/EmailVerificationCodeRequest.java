package subscribers.clearbunyang.global.email.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationCodeRequest {
    private String email;
    private String verificationCode;
}
