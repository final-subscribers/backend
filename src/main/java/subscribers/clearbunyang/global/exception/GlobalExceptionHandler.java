package subscribers.clearbunyang.global.exception;


import jakarta.annotation.Priority;
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
    public Response<Void> methodArgumentTypeException(MethodArgumentTypeMismatchException e) {
        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        return Response.<Void>builder().result(Result.Error(errorCode)).build();
    }
}
