package subscribers.clearbunyang.domain.auth.dto.request;


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

    private String address;

    private String business;

    private FileInfo housingFile;

    private FileInfo registrationFile;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FileInfo {
        private String name;
        private String url;
        private String type;
    }
}
