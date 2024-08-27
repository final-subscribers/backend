package subscribers.clearbunyang.domain.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import subscribers.clearbunyang.domain.file.entity.File;
import subscribers.clearbunyang.domain.file.repository.FileRepository;
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

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private AdminRepository adminRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private JwtTokenProvider jwtTokenProvider;

  @Mock
  private JwtTokenService jwtTokenService;

  @Mock
  private FileRepository fileRepository;

  @Mock
  private AuthEmailService authEmailService;

  @Mock
  private AuthSmsService authSmsService;

  @InjectMocks
  private AuthService authService;

  @Value("${spring.security.redirect-uri}")
  private String logoutRedirectUri;

  @Test
  void givenAccessToken_whenLogout_thenRedirectUriReturned() {
    // 로그아웃 후 Uri 리다이렉트
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    Cookie accessTokenCookie = new Cookie("accessToken", "sampleAccessToken");
    when(request.getCookies()).thenReturn(new Cookie[]{accessTokenCookie});
    when(jwtTokenProvider.getEmailFromToken("sampleAccessToken", JwtTokenType.ACCESS)).thenReturn("user@example.com");
    when(adminRepository.existsByEmail("user@example.com")).thenReturn(true);
    when(adminRepository.findByEmail("user@example.com")).thenReturn(Optional.of(new Admin()));

    String result = authService.logout(request, response);

    assertEquals(logoutRedirectUri, result);
  }

  @Test
  void testLogout_InvalidToken() {
    // 유효하지 않은 토큰 로그아웃
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    when(request.getCookies()).thenReturn(new Cookie[]{});
    lenient().when(jwtTokenProvider.getEmailFromToken(anyString(), any(JwtTokenType.class)))
        .thenThrow(new InvalidValueException(ErrorCode.INVALID_ACCESS_TOKEN));

    assertThrows(InvalidValueException.class, () -> authService.logout(request, response));
  }

  @Test
  void testAdminSignup_Success() {
    //어드민 회원가입 성공
    AdminSignUpRequest request = new AdminSignUpRequest(
        "Admin Name",
        "admin@example.com",
        "password",
        "1234567890",
        "Company Name",
        123456789L,
        "Address",
        "Business",
        new AdminSignUpRequest.FileInfo("housingFile", "url", "HOUSING"),
        new AdminSignUpRequest.FileInfo("registrationFile", "url", "REGISTRATION")
    );

    when(adminRepository.existsByEmail(request.getEmail())).thenReturn(false);
    doNothing().when(authEmailService).isVerified(request);

    authService.admnSignup(request);

    verify(adminRepository).save(any(Admin.class));
    verify(fileRepository, times(2)).save(any(File.class));
  }

  @Test
  void testAdminSignup_EmailDuplication() {
    //어드민 이메일 중복
    AdminSignUpRequest request = AdminSignUpRequest.builder()
        .email("john@example.com")
        .build();

    when(adminRepository.existsByEmail(request.getEmail())).thenReturn(true);

    InvalidValueException thrown = assertThrows(InvalidValueException.class, () -> {
      authService.admnSignup(request);
    });

    assertEquals(ErrorCode.EMAIL_DUPLICATION, thrown.getErrorCode());
  }


  @Test
  void testMemberSignup_Success() {
    //사용자 회원가입 성공
    MemberSignUpRequest request = MemberSignUpRequest.builder()
        .name("Jane Doe")
        .email("jane@example.com")
        .password("password")
        .phoneNumber("0987654321")
        .address("456 Avenue")
        .build();

    when(memberRepository.existsByEmail(request.getEmail())).thenReturn(false);
    when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

    authService.memberSignup(request);

    verify(authSmsService).isVerified(request);
    verify(memberRepository).save(any(Member.class));
  }

  @Test
  void testMemberSignup_EmailDuplication() {
    //사용자 이메일 중복
    MemberSignUpRequest request = MemberSignUpRequest.builder()
        .email("jane@example.com")
        .build();

    when(memberRepository.existsByEmail(request.getEmail())).thenReturn(true);

    InvalidValueException thrown = assertThrows(InvalidValueException.class, () -> {
      authService.memberSignup(request);
    });

    assertEquals(ErrorCode.EMAIL_DUPLICATION, thrown.getErrorCode());
  }

  @Test
  void testLogin_Admin_Success() {
    //관리자 로그인 성공
    LoginRequest request = LoginRequest.builder()
        .email("admin@example.com")
        .password("password")
        .build();

    Admin admin = Admin.builder()
        .email("admin@example.com")
        .password(passwordEncoder.encode("password"))
        .status(AdminState.ACCEPTED)
        .role(UserRole.ADMIN)
        .build();

    when(adminRepository.existsByEmail(request.getEmail())).thenReturn(true);
    when(adminRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(admin));
    when(passwordEncoder.matches(request.getPassword(), admin.getPassword())).thenReturn(true);
    when(jwtTokenProvider.createToken(any(), any(), any())).thenReturn("token");
    doNothing().when(jwtTokenService).saveRefreshToken(any(), any());

    LoginResponse response = authService.login(request);

    assertNotNull(response.getAccessToken());
    assertNotNull(response.getRefreshToken());
  }

  @Test
  void testLogin_Member_Success() {
    //사용자 로그인 성공
    LoginRequest request = LoginRequest.builder()
        .email("member@example.com")
        .password("password")
        .build();

    Member member = Member.builder()
        .email("member@example.com")
        .password(passwordEncoder.encode("password"))
        .role(UserRole.MEMBER)
        .build();

    when(memberRepository.existsByEmail(request.getEmail())).thenReturn(true);
    when(memberRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(member));
    when(passwordEncoder.matches(request.getPassword(), member.getPassword())).thenReturn(true);
    when(jwtTokenProvider.createToken(any(), any(), any())).thenReturn("token");
    doNothing().when(jwtTokenService).saveRefreshToken(any(), any());

    LoginResponse response = authService.login(request);

    assertNotNull(response.getAccessToken());
    assertNotNull(response.getRefreshToken());
  }

  @Test
  void testLogin_PasswordMismatch() {
    //비밀번호 불일치 로그인 실패
    LoginRequest request = LoginRequest.builder()
        .email("admin@example.com")
        .password("wrongpassword")
        .build();

    Admin admin = Admin.builder()
        .email("admin@example.com")
        .password(passwordEncoder.encode("password"))
        .build();

    when(adminRepository.existsByEmail(request.getEmail())).thenReturn(true);
    when(adminRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(admin));
    when(passwordEncoder.matches(request.getPassword(), admin.getPassword())).thenReturn(false);

    InvalidValueException thrown = assertThrows(InvalidValueException.class, () -> {
      authService.login(request);
    });

    assertEquals(ErrorCode.PASSWORD_MISMATCH, thrown.getErrorCode());
  }

  @Test
  void testAddTokenCookies() {
    //쿠키 추가
    HttpServletResponse response = mock(HttpServletResponse.class);
    String accessToken = "sampleAccessToken";
    String refreshToken = "sampleRefreshToken";

    try (MockedStatic<CookieUtil> mockedCookieUtil = mockStatic(CookieUtil.class)) {

      authService.addTokenCookies(response, accessToken, refreshToken);

      mockedCookieUtil.verify(() -> CookieUtil.addCookie(
          eq(response),
          eq("accessToken"),
          eq(accessToken),
          anyLong()
      ), times(1));
      mockedCookieUtil.verify(() -> CookieUtil.addCookie(
          eq(response),
          eq("refreshToken"),
          eq(refreshToken),
          anyLong()
      ), times(1));
    }
  }

  @Test
  void testStandardLogout_Success() {
    // 로그아웃 성공
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    Cookie accessTokenCookie = new Cookie("accessToken", "sampleAccessToken");

    when(request.getCookies()).thenReturn(new Cookie[]{accessTokenCookie});
    when(jwtTokenProvider.getEmailFromToken("sampleAccessToken", JwtTokenType.ACCESS))
        .thenReturn("member@example.com");

    when(adminRepository.existsByEmail("member@example.com")).thenReturn(true);
    when(adminRepository.findByEmail("member@example.com"))
        .thenReturn(Optional.of(new Admin()));

    authService.standardLogout(request, response);

    verify(response).addHeader("Set-Cookie", "accessToken=; Path=/; Max-Age=0; Expires=Thu, 01 Jan 1970 00:00:00 GMT; Secure; HttpOnly; SameSite=None");
    verify(response).addHeader("Set-Cookie", "refreshToken=; Path=/; Max-Age=0; Expires=Thu, 01 Jan 1970 00:00:00 GMT; Secure; HttpOnly; SameSite=None");
  }

  @Test
  void testStandardLogout_UserNotFound() {
    //없는유저 로그아웃 실패
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    Cookie accessTokenCookie = new Cookie("accessToken", "sampleAccessToken");

    when(request.getCookies()).thenReturn(new Cookie[]{accessTokenCookie});
    when(jwtTokenProvider.getEmailFromToken("sampleAccessToken", JwtTokenType.ACCESS))
        .thenReturn("user@example.com");
    when(adminRepository.existsByEmail("user@example.com")).thenReturn(false);
    when(memberRepository.existsByEmail("user@example.com")).thenReturn(false);

    assertThrows(EntityNotFoundException.class, () -> authService.standardLogout(request, response));
  }

  @Test
  void testDeleteTokenCookies() {
    //쿠키 삭제
    HttpServletResponse response = mock(HttpServletResponse.class);

    try (MockedStatic<CookieUtil> mockedCookieUtil = mockStatic(CookieUtil.class)) {

      authService.deleteTokenCookies(response);

      mockedCookieUtil.verify(() -> CookieUtil.deleteCookie(eq(response), eq("accessToken")),
          times(1));
      mockedCookieUtil.verify(() -> CookieUtil.deleteCookie(eq(response), eq("refreshToken")),
          times(1));
    }
  }
}