package subscribers.clearbunyang.domain.property.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import subscribers.clearbunyang.domain.property.model.request.PropertyRequestDTO;
import subscribers.clearbunyang.domain.property.model.response.MyPropertyCardResponseDTO;
import subscribers.clearbunyang.domain.property.service.PropertyService;
import subscribers.clearbunyang.global.model.PagedDto;
import subscribers.clearbunyang.global.security.details.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/common")
public class AdminPropertyController {

    private final PropertyService propertyService;

    /**
     * 물건을 저장하는 메소드
     *
     * @param requestDTO
     * @param CustomUserDetails jwt 토큰으로 받은 user 객체
     * @return
     */
    @PostMapping("/properties")
    public void addProperty(
            @Valid @RequestBody PropertyRequestDTO requestDTO,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        propertyService.saveProperty(requestDTO, customUserDetails.getUserId());
    }

    /**
     * 내가 등록한 매물의 첫번째 페이지네이션을 리턴하는 메소드
     *
     * @param page 현재 페이지(0부터 시작)
     * @param size 한 페이지당 보일 객체 수
     * @param customUserDetails
     * @return
     */
    @GetMapping("/my-properties/card")
    public PagedDto<MyPropertyCardResponseDTO> getCommentsByOffset(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        //        Long adminId = customUserDetails.getUserId();
        //        return propertyService.getCards(page,size,adminId);
        return propertyService.getCards(page, size, 1L);
    }
}
