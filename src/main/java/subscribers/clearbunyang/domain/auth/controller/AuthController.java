package subscribers.clearbunyang.domain.auth.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subscribers.clearbunyang.domain.auth.service.AuthService;
import subscribers.clearbunyang.domain.user.model.request.AdminSignUpRequest;
import subscribers.clearbunyang.domain.user.model.request.LoginRequest;
import subscribers.clearbunyang.domain.user.model.request.MemberSignUpRequest;
import subscribers.clearbunyang.domain.user.model.response.LoginResponse;
import subscribers.clearbunyang.global.email.model.EmailVerificationCodeRequest;
import subscribers.clearbunyang.global.email.model.EmailVerificationRequest;
import subscribers.clearbunyang.global.sms.model.SmsCertificationRequest;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/admin-signup")
    public ResponseEntity<String> adminSignUp(@Valid @RequestBody AdminSignUpRequest request) {
        authService.admnSignup(request);
        return ResponseEntity.ok("관리자 회원가입 성공");
    }

    @PostMapping("/register/send-verification-code")
    public ResponseEntity<String> sendVerificationCode(
            @RequestBody EmailVerificationRequest request) {
        authService.sendVerificationCode(request.getEmail());
        return ResponseEntity.ok("인증코드가 이메일로 전송되었습니다.");
    }

    @PostMapping("/register/verify-email")
    public ResponseEntity<String> verifyCode(@RequestBody EmailVerificationCodeRequest request) {
        authService.verifyCode(request.getEmail(), request.getVerificationCode());
        return ResponseEntity.ok("이메일 인증 성공");
    }

    @PostMapping("/user-signup")
    public ResponseEntity<String> userSignUp(@Valid @RequestBody MemberSignUpRequest request) {
        authService.userSignup(request);
        return ResponseEntity.ok("사용자 회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = authService.login(request);
        authService.addTokenCookies(
                response, loginResponse.getAccessToken(), loginResponse.getRefreshToken());
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String redirectUri = authService.logout(request, response);
        response.sendRedirect(redirectUri);
    }

    @PostMapping("/register/send-verification-sms-code")
    public ResponseEntity<String> sendSms(@RequestBody SmsCertificationRequest request) {
        authService.sendSms(request);
        return ResponseEntity.ok("인증코드가 문자로 전송되었습니다.");
    }

    @PostMapping("/register/verify-sms")
    public ResponseEntity<String> smsVerification(@RequestBody SmsCertificationRequest request) {
        authService.verifySms(request);
        return ResponseEntity.ok("문자 인증 성공");
    }
}
