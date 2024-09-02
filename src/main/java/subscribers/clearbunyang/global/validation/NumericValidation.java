package subscribers.clearbunyang.global.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NumericValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NumericValidation {
    String message() default "String 타입이지만 값은 숫자 형식으로 입력되어야함";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
