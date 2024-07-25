package subscribers.clearbunyang.global.exception.Invalid;

import subscribers.global.exception.CustomException;
import subscribers.global.exception.error.ErrorCode;

public class InvalidValueException extends CustomException {
    public InvalidValueException(ErrorCode errorCode){ super(errorCode); }
}
