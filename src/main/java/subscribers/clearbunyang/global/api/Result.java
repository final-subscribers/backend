package subscribers.clearbunyang.global.api;


import io.jsonwebtoken.lang.Assert;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Result {
    private Integer resultCode;
    private String resultMessage;

    public static Result OK() {
        return Result.builder().resultCode(HttpStatus.OK.value()).resultMessage("성공").build();
    }

    private static String concatErrorCodeAndMessage(ErrorCode errorCode) {
        return errorCode.getCode() + ", " + errorCode.getMessage();
    }

    public static Result Error(ErrorCode errorCode) {
        return Result.builder()
                .resultCode(errorCode.getStatus())
                .resultMessage(concatErrorCodeAndMessage(errorCode))
                .build();
    }

    @Builder
    private Result(Integer resultCode, String resultMessage) {
        Assert.notNull(resultCode, "resultCode must not be null");
        Assert.notNull(resultMessage, "resultMessage must not be null");
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
    }
}
