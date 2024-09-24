package subscribers.clearbunyang.domain.auth.service;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.domain.auth.dto.request.AdminSignUpRequest;
import subscribers.clearbunyang.domain.auth.dto.request.LoginRequest;
import subscribers.clearbunyang.domain.auth.dto.request.MemberSignUpRequest;
import subscribers.clearbunyang.domain.auth.dto.response.LoginResponse;
import subscribers.clearbunyang.domain.auth.entity.Admin;
import subscribers.clearbunyang.domain.auth.entity.Member;
import subscribers.clearbunyang.domain.auth.entity.enums.AdminState;
import subscribers.clearbunyang.domain.auth.entity.enums.UserRole;
import subscribers.clearbunyang.domain.auth.repository.AdminRepository;
import subscribers.clearbunyang.domain.auth.repository.MemberRepository;
import subscribers.clearbunyang.global.exception.EntityNotFoundException;
import subscribers.clearbunyang.global.exception.InvalidValueException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;
import subscribers.clearbunyang.global.file.entity.File;
import subscribers.clearbunyang.global.file.entity.enums.FileType;
import subscribers.clearbunyang.global.file.repository.FileRepository;
import subscribers.clearbunyang.global.security.token.JwtTokenProvider;
import subscribers.clearbunyang.global.security.token.JwtTokenService;
import subscribers.clearbunyang.global.security.token.JwtTokenType;
// import subscribers.clearbunyang.global.security.util.CookieUtil;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenService jwtTokenService;
    private final FileRepository fileRepository;
    private final AuthEmailService authEmailService;
    private final AuthSmsService authSmsService;

    // 추후 main페이지로 uri값 수정해야함
    @Value("${spring.security.redirect-uri}") private String logoutRedirectUri;

    @Transactional
    public String logout(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String accessToken = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            accessToken = authorizationHeader.substring(7);
            log.info("로그인방식: Bearer " + accessToken);
        } else {
            throw new InvalidValueException(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        log.info("일반 로그아웃");
        standardLogout(request);
        return logoutRedirectUri;
    }

    @Transactional
    public void admnSignup(AdminSignUpRequest request) {
        log.info("관리자 회원가입 시도: 이메일={}, 이름={}", request.getEmail(), request.getName());

        if (adminRepository.existsByEmail(request.getEmail())) {
            throw new InvalidValueException(ErrorCode.EMAIL_DUPLICATION);
        }

        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new InvalidValueException(ErrorCode.EMAIL_DUPLICATION);
        }

        authEmailService.isVerified(request);

        Admin admin =
                Admin.builder()
                        .name(request.getName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .phoneNumber(request.getPhoneNumber())
                        .companyName(request.getCompanyName())
                        .address(request.getAddress())
                        .business(request.getBusiness())
                        .status(AdminState.ACCEPTED)
                        .role(UserRole.ADMIN)
                        .build();

        adminRepository.save(admin);

        saveFiles(request.getHousingFile(), request.getRegistrationFile(), admin);

        log.info("관리자 회원가입 성공: 이메일={}", admin.getEmail());
    }

    private void saveFiles(
            AdminSignUpRequest.FileInfo housingFileInfo,
            AdminSignUpRequest.FileInfo registrationFileInfo,
            Admin admin) {

        if (housingFileInfo == null && registrationFileInfo == null) {
            throw new InvalidValueException(ErrorCode.FILE_INFO_REQUIRED);
        }

        if (housingFileInfo != null) {

            if (housingFileInfo.getName() == null
                    || housingFileInfo.getUrl() == null
                    || housingFileInfo.getType() == null) {
                throw new InvalidValueException(ErrorCode.INVALID_FILE_INFO);
            }

            File housingFile =
                    File.builder()
                            .admin(admin)
                            .name(housingFileInfo.getName())
                            .link(housingFileInfo.getUrl())
                            .type(FileType.valueOf(housingFileInfo.getType()))
                            .build();

            fileRepository.save(housingFile);
        }

        if (registrationFileInfo != null) {

            if (registrationFileInfo.getName() == null
                    || registrationFileInfo.getUrl() == null
                    || registrationFileInfo.getType() == null) {
                throw new InvalidValueException(ErrorCode.INVALID_FILE_INFO);
            }

            File registrationFile =
                    File.builder()
                            .admin(admin)
                            .name(registrationFileInfo.getName())
                            .link(registrationFileInfo.getUrl())
                            .type(FileType.valueOf(registrationFileInfo.getType()))
                            .build();

            fileRepository.save(registrationFile);
        }
    }

    @Transactional
    public void memberSignup(MemberSignUpRequest request) {
        log.info("사용자 회원가입 시도: 이메일={}, 이름={}", request.getEmail(), request.getName());

        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new InvalidValueException(ErrorCode.EMAIL_DUPLICATION);
        }

        if (adminRepository.existsByEmail(request.getEmail())) {
            throw new InvalidValueException(ErrorCode.EMAIL_DUPLICATION);
        }

        authEmailService.isEmailVerified(request);

        authSmsService.isVerified(request);

        Member member =
                Member.builder()
                        .name(request.getName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .phoneNumber(request.getPhoneNumber())
                        .role(UserRole.MEMBER)
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
            return memberLogin(request);
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

        String accessToken =
                jwtTokenProvider.createToken(
                        admin.getEmail(), admin.getRole().name(), JwtTokenType.ACCESS);
        String refreshToken =
                jwtTokenProvider.createToken(
                        admin.getEmail(), admin.getRole().name(), JwtTokenType.REFRESH);
        jwtTokenService.saveRefreshToken(admin.getEmail(), refreshToken);

        log.info(
                "관리자 로그인 성공: 이메일={}, AccessToken={}, RefreshToken={}",
                admin.getEmail(),
                accessToken,
                refreshToken);
        return LoginResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    private LoginResponse memberLogin(LoginRequest request) {
        Member member =
                memberRepository
                        .findByEmail(request.getEmail())
                        .orElseThrow(() -> new InvalidValueException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new InvalidValueException(ErrorCode.PASSWORD_MISMATCH);
        }

        String accessToken =
                jwtTokenProvider.createToken(
                        member.getEmail(), member.getRole().name(), JwtTokenType.ACCESS);
        String refreshToken =
                jwtTokenProvider.createToken(
                        member.getEmail(), member.getRole().name(), JwtTokenType.REFRESH);
        jwtTokenService.saveRefreshToken(member.getEmail(), refreshToken);

        log.info(
                "사용자 로그인 성공: 이메일={}, AccessToken={}, RefreshToken={}",
                member.getEmail(),
                accessToken,
                refreshToken);
        return LoginResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    //    public void addTokenCookies(
    //            HttpServletResponse response, String accessToken, String refreshToken) {
    //        CookieUtil.addCookie(
    //                response, "accessToken", accessToken, JwtTokenType.ACCESS.getExpireTime() /
    // 1000);
    //        CookieUtil.addCookie(
    //                response,
    //                "refreshToken",
    //                refreshToken,
    //                JwtTokenType.REFRESH.getExpireTime() / 1000);
    //    }

    @Transactional
    public void standardLogout(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        String accessToken = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            accessToken = authorizationHeader.substring(7);
        }

        if (accessToken == null) {
            throw new InvalidValueException(ErrorCode.INVALID_TOKEN);
        }

        String email = jwtTokenProvider.getEmailFromToken(accessToken, JwtTokenType.ACCESS);

        if (adminRepository.existsByEmail(email)) {
            adminRepository
                    .findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));
        } else if (memberRepository.existsByEmail(email)) {
            memberRepository
                    .findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));
        } else {
            throw new EntityNotFoundException(ErrorCode.USER_NOT_FOUND);
        }

        log.info("로그아웃 성공: 이메일={}", email);
    }

    //    public void deleteTokenCookies(HttpServletResponse response) {
    //        CookieUtil.deleteCookie(response, "accessToken");
    //        CookieUtil.deleteCookie(response, "refreshToken");
    //    }
}
