package subscribers.clearbunyang.domain.user.model.request;


import java.math.BigInteger;
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
public class MemberSignUpRequest {

    private String name;

    @EmailValidation private String email;

    @PasswordValidation private String password;

    private BigInteger phoneNumber;

    private String address;
}
