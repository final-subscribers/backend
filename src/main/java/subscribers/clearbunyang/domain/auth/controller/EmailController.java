package subscribers.clearbunyang.domain.auth.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subscribers.clearbunyang.domain.auth.dto.request.EmailVerificationCodeRequest;
import subscribers.clearbunyang.domain.auth.dto.request.EmailVerificationRequest;
import subscribers.clearbunyang.domain.auth.service.AuthEmailService;

@RestController
@RequestMapping("/api/auth/register")
@RequiredArgsConstructor
@Tag(name = "Email", description = "이메일 인증관련")
public class EmailController {

    private final AuthEmailService authEmailService;

    @Operation(summary = "이메일 인증코드 보내기")
    @PostMapping("/send-email-code")
    public ResponseEntity<String> sendVerificationCode(
            @RequestBody EmailVerificationRequest request) {
        authEmailService.sendVerificationCode(request.getEmail());
        return ResponseEntity.ok("인증코드가 이메일로 전송되었습니다.");
    }

    @Operation(summary = "어드민 이메일 인증")
    @PostMapping("/verify-admin-email")
    public ResponseEntity<String> verifyCode(@RequestBody EmailVerificationCodeRequest request) {
        authEmailService.verifyCode(request.getEmail(), request.getVerificationCode());
        return ResponseEntity.ok("이메일 인증 성공");
    }

    @Operation(summary = "일반유저 이메일 중복확인")
    @PostMapping("/verify-member-email")
    public ResponseEntity<String> verifyEmail(@RequestBody EmailVerificationRequest request) {
        authEmailService.verifyEmail(request.getEmail());
        return ResponseEntity.ok("이메일 인증 성공");
    }
}
