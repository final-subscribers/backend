package subscribers.clearbunyang.domain.auth.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subscribers.clearbunyang.domain.auth.dto.response.UserInfoResponse;
import subscribers.clearbunyang.global.security.details.CustomUserDetails;

@RestController
@RequestMapping("/api/common")
@RequiredArgsConstructor
@Tag(name = "Common", description = "회원정보 조회")
public class UserInfoController {

    @Operation(summary = "로그인 회원정보 조회")
    @GetMapping("/my-information")
    public ResponseEntity<UserInfoResponse> getUserInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(userDetails.getUserInfo());
    }
}
