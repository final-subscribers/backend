package subscribers.clearbunyang.domain.user.model.request;


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
public class AdminSignUpRequest {

    private String name;

    @EmailValidation private String email;

    @PasswordValidation private String password;

    private String phoneNumber;

    private String companyName;

    private Long registrationNumber;

    private String address;

    private String business;

    private String housingFileUrl;

    private String registrationFileUrl;
}
