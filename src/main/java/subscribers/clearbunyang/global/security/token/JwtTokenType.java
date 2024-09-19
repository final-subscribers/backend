package subscribers.clearbunyang.global.security.token;


import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
// @RequiredArgsConstructor
public enum JwtTokenType {
    //    ACCESS("ACCESS", 30 * 60 * 1000L), // 30분
    //    REFRESH("REFRESH", 7 * 24 * 60 * 60 * 1000L); // 7일
    ACCESS("ACCESS"),
    REFRESH("REFRESH");

    private final String type;
    private Long expireTime;

    JwtTokenType(String type) {
        this.type = type;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    @Component
    public static class JwtTokenTypeInitializer {

        private final JwtTokenTime tokenTime;

        public JwtTokenTypeInitializer(JwtTokenTime tokenTime) {
            this.tokenTime = tokenTime;
            initialize();
        }

        @PostConstruct
        private void initialize() {
            ACCESS.setExpireTime(tokenTime.getAccessTokenExpireTime());
            REFRESH.setExpireTime(tokenTime.getRefreshTokenExpireTime());
        }
    }
}
