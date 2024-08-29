package subscribers.clearbunyang.global.exception;


import jakarta.annotation.Priority;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import subscribers.clearbunyang.global.api.Response;
import subscribers.clearbunyang.global.api.Result;
import subscribers.clearbunyang.global.exception.Invalid.InvalidValueException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;
import subscribers.clearbunyang.global.exception.notFound.EntityNotFoundException;

@Slf4j
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

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Response<Object>> ValidationException(
            MethodArgumentNotValidException exception) {
        log.error("", exception);

        return ResponseEntity.status(ErrorCode.BAD_REQUEST.getStatus())
                .body(Response.ERROR(ErrorCode.BAD_REQUEST));
    }

    /**
     * 정의한 enum 이와의 값을 입력했을때 발생하는 예외
     *
     * @param exception
     * @return
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<Response<Object>> HttpMessageNotReadableException(
            HttpMessageNotReadableException exception) {
        log.error("", exception);

        return ResponseEntity.status(ErrorCode.BAD_REQUEST.getStatus())
                .body(Response.ERROR(ErrorCode.BAD_REQUEST));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Response<Object>> ValidationException(
            MissingServletRequestParameterException exception) {
        log.error("", exception);

        return ResponseEntity.status(ErrorCode.BAD_REQUEST.getStatus())
                .body(Response.ERROR(ErrorCode.BAD_REQUEST));
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
    /* @ExceptionHandler(MethodArgumentNotValidException.class)
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
    }*/
}
