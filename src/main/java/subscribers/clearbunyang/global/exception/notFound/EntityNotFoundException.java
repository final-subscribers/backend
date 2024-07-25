package subscribers.clearbunyang.global.exception.notFound;

import subscribers.global.exception.CustomException;
import subscribers.global.exception.error.ErrorCode;

public class EntityNotFoundException extends CustomException {
    public EntityNotFoundException(ErrorCode errorCode) { super(errorCode); }
}
