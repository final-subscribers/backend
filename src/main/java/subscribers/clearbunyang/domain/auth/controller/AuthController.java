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

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/adminSignUp")
    public ResponseEntity<String> adminSignUp(@Valid @RequestBody AdminSignUpRequest request) {
        authService.admnSignup(request);
        return ResponseEntity.ok("관리자 회원가입 성공");
    }

    @PostMapping("/userSignUp")
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

    @GetMapping("/refreshtoken")
    public ResponseEntity<String> refreshToken(
            HttpServletRequest request, HttpServletResponse response) {
        authService.refreshToken(request, response);
        return ResponseEntity.ok("엑세스 토큰 재발급 성공");
    }
}
