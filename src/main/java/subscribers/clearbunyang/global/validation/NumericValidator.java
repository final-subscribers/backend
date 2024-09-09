package subscribers.clearbunyang.global.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NumericValidator implements ConstraintValidator<NumericValidation, String> {

    @Override
    public void initialize(NumericValidation constraintAnnotation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.matches("\\d+"); // 숫자로만 구성되어 있는지 검증
    }
}
