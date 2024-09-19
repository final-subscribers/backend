package subscribers.clearbunyang.global.security.token;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import subscribers.clearbunyang.global.security.util.CookieUtil;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${spring.jwt.access-secret}") private String accessSecret;

    @Value("${spring.jwt.refresh-secret}") private String refreshSecret;

    private Key accessKey;
    private Key refreshKey;

    @PostConstruct
    public void init() {
        byte[] accessKeyBytes = Decoders.BASE64.decode(accessSecret);
        byte[] refreshKeyBytes = Decoders.BASE64.decode(refreshSecret);
        this.accessKey = Keys.hmacShaKeyFor(accessKeyBytes);
        this.refreshKey = Keys.hmacShaKeyFor(refreshKeyBytes);
    }

    public String createToken(String email, String role, JwtTokenType type) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + type.getExpireTime());

        Key key = type == JwtTokenType.ACCESS ? accessKey : refreshKey;

        Claims claims = Jwts.claims();
        claims.setSubject(email);
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token, JwtTokenType type) {
        try {
            Key key = type == JwtTokenType.ACCESS ? accessKey : refreshKey;
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getUserInfoFromToken(String token, JwtTokenType type) {
        Key key = type == JwtTokenType.ACCESS ? accessKey : refreshKey;
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String resolveToken(HttpServletRequest request) {
        Cookie accessTokenCookie = CookieUtil.getCookie(request, "accessToken");
        if (accessTokenCookie != null) {
            return accessTokenCookie.getValue();
        }
        return null;
    }

    public String getEmailFromToken(String token, JwtTokenType type) {
        return getUserInfoFromToken(token, type).getSubject();
    }
}
