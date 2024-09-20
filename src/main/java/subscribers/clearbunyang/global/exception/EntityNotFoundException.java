package subscribers.clearbunyang.global.exception;


import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;

public class EntityNotFoundException extends NotFoundException {
    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
