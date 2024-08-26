package subscribers.clearbunyang.domain.user.model.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @JsonProperty("phone_number")
    @Setter
    private String phoneNumber;

    private String address;
}
