package subscribers.clearbunyang.domain.auth.service;


import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.domain.user.entity.Admin;
import subscribers.clearbunyang.domain.user.entity.Member;
import subscribers.clearbunyang.domain.user.entity.enums.AdminState;
import subscribers.clearbunyang.domain.user.entity.enums.UserRole;
import subscribers.clearbunyang.domain.user.model.request.AdminSignUpRequest;
import subscribers.clearbunyang.domain.user.model.request.LoginRequest;
import subscribers.clearbunyang.domain.user.model.request.MemberSignUpRequest;
import subscribers.clearbunyang.domain.user.model.response.LoginResponse;
import subscribers.clearbunyang.domain.user.repository.AdminRepository;
import subscribers.clearbunyang.domain.user.repository.MemberRepository;
import subscribers.clearbunyang.global.exception.Invalid.InvalidValueException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;
import subscribers.clearbunyang.global.exception.notFound.EntityNotFoundException;
import subscribers.clearbunyang.global.token.JwtTokenProvider;
import subscribers.clearbunyang.global.token.JwtTokenService;
import subscribers.clearbunyang.global.token.JwtTokenType;
import subscribers.clearbunyang.global.util.CookieUtil;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenService jwtTokenService;

    // 추후 main페이지로 uri값 수정해야함
    @Value("${spring.security.redirect-uri}") private String logoutRedirectUri;

    @Transactional
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        String cookieName = CookieUtil.getCookieNames(request);
        log.info("로그인 방식: {}", cookieName);

        if ("accessToken".equals(cookieName)) {
            log.info("일반 로그아웃");
            standardLogout(request, response);
            return logoutRedirectUri;
        } else {
            throw new InvalidValueException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }

    @Transactional
    public void admnSignup(AdminSignUpRequest request) {
        log.info("관리자 회원가입 시도: 이메일={}, 이름={}", request.getEmail(), request.getName());

        if (adminRepository.existsByEmail(request.getEmail())) {
            throw new InvalidValueException(ErrorCode.EMAIL_DUPLICATION);
        }

        Admin admin =
                Admin.builder()
                        .name(request.getName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .phoneNumber(request.getPhoneNumber())
                        .companyName(request.getCompanyName())
                        .registrationNumber(request.getRegistrationNumber())
                        .address(request.getAddress())
                        .business(request.getBusiness())
                        .status(AdminState.PENDING)
                        .role(UserRole.ADMIN)
                        .build();

        adminRepository.save(admin);

        log.info("관리자 회원가입 성공: 이메일={}", admin.getEmail());
    }

    @Transactional
    public void userSignup(MemberSignUpRequest request) {
        log.info("사용자 회원가입 시도: 이메일={}, 이름={}", request.getEmail(), request.getName());

        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new InvalidValueException(ErrorCode.EMAIL_DUPLICATION);
        }

        Member member =
                Member.builder()
                        .name(request.getName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .phoneNumber(request.getPhoneNumber())
                        .address(request.getAddress())
                        .role(UserRole.USER)
                        .build();

        memberRepository.save(member);

        log.info("사용자 회원가입 성공: 이메일={}", member.getEmail());
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        log.info("로그인 시도: 이메일={}", request.getEmail());
        if (adminRepository.existsByEmail(request.getEmail())) {
            return adminLogin(request);
        } else if (memberRepository.existsByEmail(request.getEmail())) {
            return userLogin(request);
        } else {
            throw new InvalidValueException(ErrorCode.USER_NOT_FOUND);
        }
    }

    private LoginResponse adminLogin(LoginRequest request) {
        Admin admin =
                adminRepository
                        .findByEmail(request.getEmail())
                        .orElseThrow(() -> new InvalidValueException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new InvalidValueException(ErrorCode.PASSWORD_MISMATCH);
        }

        if (admin.getStatus() != AdminState.ACCEPTED) {
            throw new InvalidValueException(ErrorCode.NOT_ACCEPTED_ADMIN);
        }

        String accessToken = jwtTokenProvider.createToken(admin.getEmail(), JwtTokenType.ACCESS);
        String refreshToken = jwtTokenProvider.createToken(admin.getEmail(), JwtTokenType.REFRESH);
        jwtTokenService.saveRefreshToken(admin.getEmail(), refreshToken);

        log.info(
                "관리자 로그인 성공: 이메일={}, AccessToken={}, RefreshToken={}",
                admin.getEmail(),
                accessToken,
                refreshToken);
        return LoginResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    private LoginResponse userLogin(LoginRequest request) {
        Member member =
                memberRepository
                        .findByEmail(request.getEmail())
                        .orElseThrow(() -> new InvalidValueException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new InvalidValueException(ErrorCode.PASSWORD_MISMATCH);
        }

        String accessToken = jwtTokenProvider.createToken(member.getEmail(), JwtTokenType.ACCESS);
        String refreshToken = jwtTokenProvider.createToken(member.getEmail(), JwtTokenType.REFRESH);
        jwtTokenService.saveRefreshToken(member.getEmail(), refreshToken);

        log.info(
                "사용자 로그인 성공: 이메일={}, AccessToken={}, RefreshToken={}",
                member.getEmail(),
                accessToken,
                refreshToken);
        return LoginResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    @Transactional
    public String standardRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie refreshTokenCookie = CookieUtil.getCookie(request, "refreshToken");
        if (refreshTokenCookie == null) {
            throw new EntityNotFoundException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }
        String newAccessToken = createAccessToken(refreshTokenCookie.getValue());
        addAccessTokenCookie(response, newAccessToken);
        log.info("재발급된 Access 토큰을 쿠키에 저장: NewAccessToken={}", newAccessToken);

        return newAccessToken;
    }

    public void addTokenCookies(
            HttpServletResponse response, String accessToken, String refreshToken) {
        CookieUtil.addCookie(
                response, "accessToken", accessToken, JwtTokenType.ACCESS.getExpireTime() / 1000);
        CookieUtil.addCookie(
                response,
                "refreshToken",
                refreshToken,
                JwtTokenType.REFRESH.getExpireTime() / 1000);
    }

    public void addAccessTokenCookie(HttpServletResponse response, String accessToken) {
        CookieUtil.addCookie(
                response, "accessToken", accessToken, JwtTokenType.ACCESS.getExpireTime() / 1000);
    }

    @Transactional
    public String createAccessToken(String refreshToken) {
        Claims claims = jwtTokenProvider.getUserInfoFromToken(refreshToken, JwtTokenType.REFRESH);
        String email = claims.getSubject();
        String storedRefreshToken = jwtTokenService.getRefreshToken(email);

        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new InvalidValueException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        if (!jwtTokenProvider.validateToken(refreshToken, JwtTokenType.REFRESH)) {
            throw new InvalidValueException(ErrorCode.INVALID_TOKEN);
        }

        String newAccessToken = jwtTokenProvider.createToken(email, JwtTokenType.ACCESS);
        log.info("Access 토큰 재발급: 이메일={}, NewAccessToken={}", email, newAccessToken);
        return newAccessToken;
    }

    @Transactional
    public void standardLogout(HttpServletRequest request, HttpServletResponse response) {

        String accessToken =
                Objects.requireNonNull(CookieUtil.getCookie(request, "accessToken")).getValue();
        String email = jwtTokenProvider.getEmailFromToken(accessToken, JwtTokenType.ACCESS);

        if (adminRepository.existsByEmail(email)) {
            Admin admin =
                    adminRepository
                            .findByEmail(email)
                            .orElseThrow(
                                    () -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));
        } else if (memberRepository.existsByEmail(email)) {
            Member member =
                    memberRepository
                            .findByEmail(email)
                            .orElseThrow(
                                    () -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));
        } else {
            throw new EntityNotFoundException(ErrorCode.USER_NOT_FOUND);
        }

        deleteTokenCookies(response);

        log.info("로그아웃 성공: 이메일={}", email);
    }

    public void deleteTokenCookies(HttpServletResponse response) {
        CookieUtil.deleteCookie(response, "accessToken");
        CookieUtil.deleteCookie(response, "refreshToken");
    }
}
