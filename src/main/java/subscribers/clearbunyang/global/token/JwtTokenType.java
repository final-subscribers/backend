package subscribers.clearbunyang.global.token;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtTokenType {
    ACCESS("ACCESS", 30 * 60 * 1000L), // 30분
    REFRESH("REFRESH", 7 * 24 * 60 * 60 * 1000L); // 7일

    private final String type;
    private final Long expireTime;
}
