package subscribers.clearbunyang.domain.consultation.annotation.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import subscribers.clearbunyang.domain.consultation.annotation.ValidTier;
import subscribers.clearbunyang.domain.consultation.entity.enums.Tier;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;

public class TierValidator implements ConstraintValidator<ValidTier, Tier> {

    private ValidTier validTier;

    @Override
    public void initialize(ValidTier constraintAnnotation) {
        this.validTier = constraintAnnotation;
    }

    @Override
    public boolean isValid(Tier value, ConstraintValidatorContext context) {
        Object[] enumValues = this.validTier.enumClass().getEnumConstants();
        if (enumValues != null) {
            for (Object enumValue : enumValues) {
                if (value == enumValue) {
                    return true;
                }
            }
        }

        String errorCode = ErrorCode.INVALID_INPUT_VALUE.getCode();
        int status = ErrorCode.INVALID_INPUT_VALUE.getStatus();
        String errorMessage = ErrorCode.INVALID_INPUT_VALUE.getMessage();

        context.buildConstraintViolationWithTemplate(
                        String.format("%s (Code: %s, Status: %d)", errorMessage, errorCode, status))
                .addConstraintViolation();

        return false;
    }
}
