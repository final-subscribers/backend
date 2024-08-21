package subscribers.clearbunyang.domain.consultation.annotation.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import subscribers.clearbunyang.domain.consultation.annotation.ValidStatus;
import subscribers.clearbunyang.domain.consultation.entity.enums.Status;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;

public class StatusValidator implements ConstraintValidator<ValidStatus, Status> {

    private ValidStatus validStatus;

    @Override
    public void initialize(ValidStatus constraintAnnotation) {
        this.validStatus = constraintAnnotation;
    }

    @Override
    public boolean isValid(Status value, ConstraintValidatorContext context) {
        Object[] enumValues = this.validStatus.enumClass().getEnumConstants();
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
