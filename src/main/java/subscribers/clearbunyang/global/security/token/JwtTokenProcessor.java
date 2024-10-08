package subscribers.clearbunyang.global.security.token;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import subscribers.clearbunyang.global.exception.EntityNotFoundException;
import subscribers.clearbunyang.global.exception.InvalidValueException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;
import subscribers.clearbunyang.global.security.details.CustomUserDetailsService;
// import subscribers.clearbunyang.global.security.util.CookieUtil;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProcessor {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenService jwtTokenService;
    private final CustomUserDetailsService customUserDetailsService;

    public void processToken(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String token = jwtTokenProvider.resolveToken(request);
        if (token != null) {
            if (jwtTokenProvider.validateToken(token, JwtTokenType.ACCESS)) {
                Claims claims = jwtTokenProvider.getUserInfoFromToken(token, JwtTokenType.ACCESS);
                Date expiration = claims.getExpiration();
                Date now = new Date();
                long diff = (expiration.getTime() - now.getTime()) / 1000;

                if (diff < 300) {
                    log.info("JWT 토큰 재발급 필요");
                    String newAccessToken = refreshToken(request, response);
                    log.info("JWT 토큰 재발급: {}", newAccessToken);
                    setAuthentication(
                            jwtTokenProvider.getEmailFromToken(
                                    newAccessToken, JwtTokenType.ACCESS));
                } else {
                    setAuthentication(
                            jwtTokenProvider.getEmailFromToken(token, JwtTokenType.ACCESS));
                }
            } else {
                handleInvalidToken(response);
            }
        }
    }

    private String refreshToken(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new EntityNotFoundException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
            }

            String refreshToken = authorizationHeader.substring(7);
            Claims claims =
                    jwtTokenProvider.getUserInfoFromToken(refreshToken, JwtTokenType.REFRESH);
            String email = claims.getSubject();
            String role = claims.get("role", String.class);
            String storedRefreshToken = jwtTokenService.getRefreshToken(email);

            if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
                throw new InvalidValueException(ErrorCode.INVALID_TOKEN);
            }

            if (!jwtTokenProvider.validateToken(refreshToken, JwtTokenType.REFRESH)) {
                throw new InvalidValueException(ErrorCode.INVALID_TOKEN);
            }

            String newAccessToken = jwtTokenProvider.createToken(email, role, JwtTokenType.ACCESS);
            response.setHeader("Authorization", "Bearer " + newAccessToken);
            log.info("Access 토큰 재발급: 이메일={}, NewAccessToken={}", email, newAccessToken);
            return newAccessToken;
        } catch (EntityNotFoundException | InvalidValueException e) {
            log.error("토큰 재발급 오류: {}", e.getMessage());
            handleInvalidToken(response);
            return null;
        }
    }

    private void setAuthentication(String email) {
        UserDetails userDetails = customUserDetailsService.loadUserByEmail(email);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void handleInvalidToken(HttpServletResponse response) throws IOException {
        sendErrorResponse(response, ErrorCode.INVALID_TOKEN);
    }

    private void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode)
            throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorCode.getStatus());
        response.getWriter().write(new ObjectMapper().writeValueAsString((errorCode)));
    }
}
