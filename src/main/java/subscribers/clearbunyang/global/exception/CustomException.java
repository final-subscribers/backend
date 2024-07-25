package subscribers.clearbunyang.global.exception;

import lombok.Getter;
import subscribers.clearbunyang.global.exception.error.ErrorCode;

@Getter
public abstract class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}