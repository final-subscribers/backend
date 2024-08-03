package subscribers.clearbunyang.global.security.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import subscribers.clearbunyang.global.token.JwtTokenProcessor;
import subscribers.clearbunyang.global.util.CookieUtil;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProcessor jwtTokenProcessor;
    // private final KakaoTokenProcessor kakaoTokenProcessor;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String cookieName = CookieUtil.getCookieNames(request);
        log.info("cookieName: {}", cookieName);

        String tokenType = determineTokenType(cookieName);

        if (tokenType != null) {
            switch (tokenType) {
                case "Jwt":
                    jwtTokenProcessor.processToken(request, response);
                    break;
                    // kakao case 추후 oauth 붙일 때 추가하기
                default:
                    log.warn("No processor found for token type: {}", tokenType);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String determineTokenType(String cookieName) {
        if (cookieName == null) return null;
        if (cookieName.equals("accessToken")) return "Jwt";
        // if (cookieName.equals("kakaoAccessToken")) return "Kakao";
        return null;
    }
}
