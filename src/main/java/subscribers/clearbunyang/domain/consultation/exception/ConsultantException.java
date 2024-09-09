package subscribers.clearbunyang.domain.consultation.exception;


import subscribers.clearbunyang.global.exception.CustomException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;

public class ConsultantException extends CustomException {

    public ConsultantException(ErrorCode errorCode) {
        super(errorCode);
    }
}
