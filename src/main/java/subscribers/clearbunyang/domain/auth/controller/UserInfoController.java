package subscribers.clearbunyang.domain.auth.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import subscribers.clearbunyang.domain.auth.dto.response.UserInfoResponse;
import subscribers.clearbunyang.global.security.details.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@Tag(name = "Member, Admin", description = "회원정보 조회")
public class UserInfoController {

    @Operation(summary = "멤버 로그인 회원정보 조회")
    @GetMapping("/api/member/my-information")
    public ResponseEntity<UserInfoResponse> getMemberInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(userDetails.getUserInfo());
    }

    @Operation(summary = "로그인 회원정보 조회")
    @GetMapping("/api/admin/my-information")
    public ResponseEntity<UserInfoResponse> getAdminInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(userDetails.getUserInfo());
    }
}
