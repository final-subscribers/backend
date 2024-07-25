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
        return Result.builder()
            .resultCode(HttpStatus.OK.value())
            .resultMessage("성공")
            .build();
    }

    //TODO: 메서드 이름을 변경하고 싶음
    // Result 객체의 message 필드를 초기화하는 private 메서드인데 이를 더 분명하게 하면 좋을 거 같음
    private static String getErrorMessageWithCode(ErrorCode errorCode) {
        return errorCode.getCode() + ", " + errorCode.getMessage();
    }

    public static Result Error(ErrorCode errorCode) {
        return Result.builder()
            .resultCode(errorCode.getStatus())
            .resultMessage(getErrorMessageWithCode(errorCode))
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
