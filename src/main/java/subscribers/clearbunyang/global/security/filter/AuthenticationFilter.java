package subscribers.clearbunyang.global.security.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import subscribers.clearbunyang.global.token.JwtTokenProcessor;
import subscribers.clearbunyang.global.util.CookieUtil;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProcessor jwtTokenProcessor;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String cookieName = CookieUtil.getCookieNames(request);
        log.info("cookieName: {}", cookieName);

        if ("accessToken".equals(cookieName)) {
            jwtTokenProcessor.processToken(request, response);
        } else {
            log.warn("No processor found for cookie name: {}", cookieName);
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN, "Unauthorized: No valid cookie found");
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        List<String> excludedPaths =
                Arrays.asList(
                        "/actuator", "/api/auth/", "/api/common/", "/swagger-ui/", "/v3/api-docs");

        return excludedPaths.stream().anyMatch(path::startsWith);
    }
}
