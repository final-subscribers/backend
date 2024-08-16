package subscribers.clearbunyang.domain.user.model.request;


import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("company_name")
    private String companyName;

    @JsonProperty("registration_number")
    private Long registrationNumber;

    private String address;

    private String business;

    @JsonProperty("housing_file")
    private FileInfo housingFile;

    @JsonProperty("registration_file")
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
