package subscribers.clearbunyang.domain.likes.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subscribers.clearbunyang.domain.likes.dto.response.LikesPageResponse;
import subscribers.clearbunyang.domain.likes.service.LikesService;
import subscribers.clearbunyang.global.security.details.CustomUserDetails;

@RestController
@RequestMapping("/api/member/")
@RequiredArgsConstructor
@Tag(name = "Likes", description = "좋아요 토글/좋아요 목록조회")
public class LikesController {

    private final LikesService likesService;

    @Operation(summary = "좋아요 토글")
    @PostMapping("properties/{propertyId}/like")
    public ResponseEntity<String> toggleLike(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long propertyId) {
        likesService.toggleLike(customUserDetails.getUserId(), propertyId);
        return ResponseEntity.ok("좋아요 성공");
    }

    @Operation(summary = "좋아요 목록조회")
    @GetMapping("my-favorites")
    public ResponseEntity<LikesPageResponse> getMyFavorites(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam("status") String status,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {

        LikesPageResponse response =
                likesService.getMyFavoriteProperties(
                        customUserDetails.getUserId(), status, page, size);

        return ResponseEntity.ok(response);
    }
}
