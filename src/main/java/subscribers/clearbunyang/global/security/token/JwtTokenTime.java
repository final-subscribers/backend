package subscribers.clearbunyang.global.security.token;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenTime {
    @Value("${spring.jwt.access-token-expire-time}") private Long accessTokenExpireTime;

    @Value("${spring.jwt.refresh-token-expire-time}") private Long refreshTokenExpireTime;

    public Long getAccessTokenExpireTime() {
        return accessTokenExpireTime;
    }

    public Long getRefreshTokenExpireTime() {
        return refreshTokenExpireTime;
    }
}
