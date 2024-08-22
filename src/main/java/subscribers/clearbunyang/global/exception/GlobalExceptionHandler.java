package subscribers.clearbunyang.global.exception;


import jakarta.annotation.Priority;
import java.util.HashMap;
import java.util.Map;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import subscribers.clearbunyang.global.api.Response;
import subscribers.clearbunyang.global.api.Result;
import subscribers.clearbunyang.global.exception.Invalid.InvalidValueException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;
import subscribers.clearbunyang.global.exception.notFound.EntityNotFoundException;

@Priority(Integer.MAX_VALUE)
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public Response<Void> handleEntityNotFoundException(EntityNotFoundException e) {
        return Response.<Void>builder().result(Result.Error(e.getErrorCode())).build();
    }

    @ExceptionHandler(InvalidValueException.class)
    public Response<Void> handleInvalidValueException(InvalidValueException e) {
        return Response.<Void>builder().result(Result.Error(e.getErrorCode())).build();
    }

    @ExceptionHandler(CustomException.class)
    public Response<Void> handleCustomException(CustomException e) {
        return Response.<Void>builder().result(Result.Error(e.getErrorCode())).build();
    }

    // getCompletedResponse 서 잘못된 양식의 tier를 입력했을 때
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Response<Map<String, String>> methodArgumentTypeException(
            MethodArgumentTypeMismatchException e) {
        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        Map<String, String> errorDetails = new HashMap<>();

        String parameterName = e.getName();
        String message =
                String.format(
                        "Invalid type for parameter '%s'. Expected: %s, Actual: %s",
                        parameterName,
                        e.getRequiredType().getSimpleName(),
                        e.getValue().getClass().getSimpleName());

        errorDetails.put("parameter", parameterName);
        errorDetails.put("error", message);

        return Response.<Map<String, String>>builder()
                .result(Result.Error(errorCode))
                .body(errorDetails)
                .build();
    }

    // Valid annotation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getAllErrors()
                .forEach(
                        (error) -> {
                            String fieldName = ((FieldError) error).getField();
                            String errorMessage = error.getDefaultMessage();
                            errors.put(fieldName, errorMessage);
                        });

        return Response.<Map<String, String>>builder()
                .result(Result.Error(ErrorCode.INVALID_INPUT_VALUE))
                .body(errors)
                .build();
    }
}
