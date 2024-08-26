package subscribers.clearbunyang.domain.auth.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subscribers.clearbunyang.domain.auth.service.AuthSmsService;
import subscribers.clearbunyang.global.sms.model.SmsCertificationCodeRequest;
import subscribers.clearbunyang.global.sms.model.SmsCertificationRequest;

@RestController
@RequestMapping("/api/auth/register")
@RequiredArgsConstructor
public class SmsController {

    private final AuthSmsService authSmsService;

    @PostMapping("/send-sms-code")
    public ResponseEntity<String> sendVerificationCode(
            @RequestBody SmsCertificationRequest request) {
        authSmsService.sendVerificationCode(request.getPhoneNumber());
        return ResponseEntity.ok("인증코드가 문자로 전송되었습니다.");
    }

    @PostMapping("/verify-sms")
    public ResponseEntity<String> verifyCode(@RequestBody SmsCertificationCodeRequest request) {
        authSmsService.verifyCode(request);
        return ResponseEntity.ok("문자 인증 성공");
    }
}
