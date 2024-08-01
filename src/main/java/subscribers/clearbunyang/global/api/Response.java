package subscribers.clearbunyang.global.api;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Response<T> {
    private Result result;
    private T body;

    public static Response<Object> OK() {
        return Response.<Object>builder().result(Result.OK()).build();
    }

    public static <T> Response<T> OK(T data) {
        return Response.<T>builder().result(Result.OK()).body(data).build();
    }

    public static Response<Object> ERROR(ErrorCode errorCode) {
        return Response.<Object>builder().result(Result.Error(errorCode)).build();
    }
}
