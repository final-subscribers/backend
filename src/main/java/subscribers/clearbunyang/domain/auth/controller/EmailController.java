package subscribers.clearbunyang.domain.auth.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subscribers.clearbunyang.domain.auth.service.AuthEmailService;
import subscribers.clearbunyang.global.email.model.EmailVerificationCodeRequest;
import subscribers.clearbunyang.global.email.model.EmailVerificationRequest;

@RestController
@RequestMapping("/api/auth/register")
@RequiredArgsConstructor
public class EmailController {

    private final AuthEmailService authEmailService;

    @PostMapping("/send-email-code")
    public ResponseEntity<String> sendVerificationCode(
            @RequestBody EmailVerificationRequest request) {
        authEmailService.sendVerificationCode(request.getEmail());
        return ResponseEntity.ok("인증코드가 이메일로 전송되었습니다.");
    }

    @PostMapping("/verify-admin-email")
    public ResponseEntity<String> verifyCode(@RequestBody EmailVerificationCodeRequest request) {
        authEmailService.verifyCode(request.getEmail(), request.getVerificationCode());
        return ResponseEntity.ok("이메일 인증 성공");
    }

    @PostMapping("/verify-member-email")
    public ResponseEntity<String> verifyEmail(@RequestBody EmailVerificationRequest request) {
        authEmailService.verifyEmail(request.getEmail());
        return ResponseEntity.ok("이메일 인증 성공");
    }
}
