package subscribers.clearbunyang.domain.consultation.annotation.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import subscribers.clearbunyang.domain.consultation.annotation.ValidMedium;
import subscribers.clearbunyang.domain.consultation.entity.enums.Medium;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;

public class MediumValidator implements ConstraintValidator<ValidMedium, Medium> {

    private ValidMedium validMedium;

    @Override
    public void initialize(ValidMedium constraintAnnotation) {
        this.validMedium = constraintAnnotation;
    }

    @Override
    public boolean isValid(Medium value, ConstraintValidatorContext context) {
        Object[] enumValues = this.validMedium.enumClass().getEnumConstants();
        if (enumValues != null) {
            for (Object enumValue : enumValues) {
                if (value == enumValue) {
                    return true;
                }
            }
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(ErrorCode.INVALID_INPUT_VALUE.getMessage())
                .addConstraintViolation();
        ;
        return false;
    }
}
