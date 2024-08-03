package subscribers.clearbunyang.domain.auth.service;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.domain.member.repository.AdminRepository;
import subscribers.clearbunyang.domain.member.repository.CompanyRepository;
import subscribers.clearbunyang.domain.member.repository.UserRepository;
import subscribers.clearbunyang.global.exception.Invalid.InvalidValueException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;
import subscribers.clearbunyang.global.token.JwtTokenProvider;
import subscribers.clearbunyang.global.util.CookieUtil;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final CompanyRepository companyRepository;
    private final StandardAuthService standardAuthService;
    private final JwtTokenProvider jwtTokenProvider;

    // 추후 main페이지로 uri값 수정해야함
    @Value("${spring.security.redirect-uri}") private String logoutRedirectUri;

    @Transactional
    public String refreshToken(HttpServletRequest request, HttpServletResponse response) {

        String cookieName = CookieUtil.getCookieNames(request);
        log.info("로그인 방식: {}", cookieName);

        if (cookieName == null) {
            throw new InvalidValueException(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        switch (cookieName) {
                // kakaoToken 따로 작성 예정
            case "accessToken" -> {
                log.info("일반 토큰 재발급");
                return standardAuthService.standardRefreshToken(request, response);
            }
        }

        throw new InvalidValueException(ErrorCode.INVALID_ACCESS_TOKEN);
    }

    @Transactional
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        String cookieName = CookieUtil.getCookieNames(request);
        log.info("로그인 방식: {}", cookieName);

        if (cookieName == null) {
            throw new InvalidValueException(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        switch (cookieName) {
                // kakaoToken 따로 작성 예정
            case "accessToken" -> {
                log.info("일반 로그아웃");
                standardAuthService.standardLogout(request, response);
                return logoutRedirectUri;
            }
        }

        throw new InvalidValueException(ErrorCode.INVALID_ACCESS_TOKEN);
    }
}
