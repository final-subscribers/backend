package subscribers.clearbunyang.domain.auth.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Auth", description = "로그인/로그아웃/회원가입")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "관리자 회원가입")
    @PostMapping("/admin-signup")
    public ResponseEntity<String> adminSignUp(@Valid @RequestBody AdminSignUpRequest request) {
        authService.admnSignup(request);
        return ResponseEntity.ok("관리자 회원가입 성공");
    }

    @Operation(summary = "사용자 회원가입")
    @PostMapping("/member-signup")
    public ResponseEntity<String> memberSignup(@Valid @RequestBody MemberSignUpRequest request) {
        authService.memberSignup(request);
        return ResponseEntity.ok("사용자 회원가입 성공");
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = authService.login(request);
        authService.addTokenCookies(
                response, loginResponse.getAccessToken(), loginResponse.getRefreshToken());
        return ResponseEntity.ok(loginResponse);
    }

    @Operation(summary = "로그아웃")
    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String redirectUri = authService.logout(request, response);
        response.sendRedirect(redirectUri);
    }
}
