package subscribers.clearbunyang.domain.like.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subscribers.clearbunyang.domain.like.service.LikesService;
import subscribers.clearbunyang.global.security.details.CustomUserDetails;

@RestController
@RequestMapping("/api/member/")
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @PostMapping("properties/{propertyId}/like")
    public ResponseEntity<String> toggleLike(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long propertyId) {
        likesService.toggleLike(customUserDetails.getUserId(), propertyId);
        return ResponseEntity.ok("좋아요 성공");
    }
}
