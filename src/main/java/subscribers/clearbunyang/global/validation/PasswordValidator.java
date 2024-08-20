package subscribers.clearbunyang.global.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordValidation, String> {

    // 비밀번호 양식에 맞게 정규표현식 수정해야함
    private static final String PASSWORD_PATTERN = "^.{8,16}$"; // 8자 이상 16자 이하

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches(PASSWORD_PATTERN);
    }
}
