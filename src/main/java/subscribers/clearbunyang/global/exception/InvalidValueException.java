package subscribers.clearbunyang.global.exception;


import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;

public class InvalidValueException extends CustomException {
    public InvalidValueException(ErrorCode errorCode) {
        super(errorCode);
    }
}
