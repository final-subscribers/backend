package subscribers.clearbunyang.global.exception;


import jakarta.annotation.Priority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<Object>> ValidationException(
            MethodArgumentNotValidException exception) {
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
}
