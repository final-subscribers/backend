package subscribers.clearbunyang.global.api;


import io.jsonwebtoken.lang.Assert;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Result {
    private Integer resultCode;
    private String resultMessage;

    private static String concatErrorCodeAndMessage(ErrorCode errorCode) {
        return errorCode.getMessage();
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
