package subscribers.clearbunyang.domain.member.model.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.global.validation.EmailValidation;
import subscribers.clearbunyang.global.validation.PasswordValidation;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequest {

    private String name;

    @EmailValidation private String email;

    @PasswordValidation private String password;

    private Integer phoneNumber;

    private String address;

    private String addressDetail;
}
