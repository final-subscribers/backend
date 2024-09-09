package subscribers.clearbunyang.domain.like.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subscribers.clearbunyang.domain.like.model.response.LikesPageResponse;
import subscribers.clearbunyang.domain.like.model.response.LikesPropertyResponse;
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

    @GetMapping("my-favorites")
    public ResponseEntity<LikesPageResponse> getMyFavorites(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam("status") String status,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {

        Page<LikesPropertyResponse> propertyPage =
                likesService.getMyFavoriteProperties(
                        customUserDetails.getUserId(), status, page, size);

        LikesPageResponse response = LikesPageResponse.fromPage(propertyPage, size, page);

        return ResponseEntity.ok(response);
    }
}
