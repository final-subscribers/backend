package subscribers.clearbunyang.domain.consultation.exception;


import subscribers.clearbunyang.global.exception.CustomException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;

public class DistributedLockException extends CustomException {

    public DistributedLockException(ErrorCode errorCode) {
        super(errorCode);
    }
}
