package subscribers.clearbunyang.domain.property.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subscribers.clearbunyang.domain.property.dto.response.HomeResponse;
import subscribers.clearbunyang.domain.property.service.HomeService;
import subscribers.clearbunyang.global.dto.PagedDto;
import subscribers.clearbunyang.global.security.details.CustomUserDetails;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Tag(name = "홈 화면", description = "메인 화면")
public class HomeController {
    private final HomeService homeService;

    @Operation(summary = "비로그인/메인 화면 호출 시 좋아요 많은 순으로 매물 출력")
    @GetMapping("/common/home")
    public PagedDto<HomeResponse> getCommonHome(
            @RequestParam(required = false, value = "page", defaultValue = "0") int page) {
        return homeService.getHome(null, page);
    }

    @Operation(summary = "로그인/메인 화면 호출 시 좋아요 많은 순으로 매물 출력")
    @GetMapping("/member/home")
    public PagedDto<HomeResponse> getMemberHome(
            @RequestParam(required = false, value = "page", defaultValue = "0") int page,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long memberId = customUserDetails.getUserId();
        return homeService.getHome(memberId, page);
    }
}
