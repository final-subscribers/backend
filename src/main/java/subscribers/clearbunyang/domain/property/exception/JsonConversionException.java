package subscribers.clearbunyang.domain.property.exception;


import subscribers.clearbunyang.global.exception.CustomException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;

public class JsonConversionException extends CustomException {

    public JsonConversionException() {
        super(ErrorCode.JSON_CONVERSION_FAILED);
    }
}
