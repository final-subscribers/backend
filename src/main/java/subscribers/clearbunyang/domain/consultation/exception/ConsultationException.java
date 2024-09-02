package subscribers.clearbunyang.domain.consultation.exception;


import subscribers.clearbunyang.global.exception.CustomException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;

public class ConsultationException extends CustomException {

    public ConsultationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
