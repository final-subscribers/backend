package subscribers.clearbunyang.global.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordValidation, String> {

    // 영문+숫자+특수문자를 모두 포함하여 10자 이상
    private static final String PASSWORD_PATTERN =
            "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{10,}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches(PASSWORD_PATTERN);
    }
}
