package subscribers.clearbunyang.global.exception;


import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;

public class NotFoundException extends CustomException {

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
