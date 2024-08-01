package subscribers.clearbunyang.global.exception.notFound;


import subscribers.clearbunyang.global.exception.CustomException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;

public class EntityNotFoundException extends CustomException {
    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
