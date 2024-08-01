package subscribers.clearbunyang.global.exception.Invalid;


import subscribers.clearbunyang.global.exception.CustomException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;

public class InvalidValueException extends CustomException {
    public InvalidValueException(ErrorCode errorCode) {
        super(errorCode);
    }
}
