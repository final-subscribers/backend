package subscribers.clearbunyang.domain.property.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import subscribers.clearbunyang.domain.property.dto.request.PropertySaveRequest;
import subscribers.clearbunyang.domain.property.dto.request.PropertyUpdateRequest;
import subscribers.clearbunyang.domain.property.dto.response.MyPropertyCardResponse;
import subscribers.clearbunyang.domain.property.dto.response.MyPropertyTableResponse;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.service.PropertyService;
import subscribers.clearbunyang.global.dto.PagedDto;
import subscribers.clearbunyang.global.security.details.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Tag(name = "매물관리", description = "어드민 마이페이지 > 매물등록/매물관리")
public class AdminPropertyController {

    private final PropertyService propertyService;

    /**
     * 물건을 저장하는 메소드
     *
     * @param requestDTO
     * @param CustomUserDetails jwt 토큰으로 받은 user 객체
     * @return
     */
    @Operation(summary = "매물등록")
    @PostMapping("/properties")
    public void addProperty(
            @Valid @RequestBody PropertySaveRequest requestDTO,
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
    @Operation(summary = "매물관리-첫번째 페이지네이션")
    @GetMapping("/my-properties/card")
    public PagedDto<MyPropertyCardResponse> getCardsByOffset(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long adminId = customUserDetails.getUserId();
        return propertyService.getCards(page, size, adminId);
    }

    /**
     * 내가 등록한 매물의 두번째 페이지네이션을 리턴하는 메소드
     *
     * @param page
     * @param size
     * @param customUserDetails
     * @return
     */
    @Operation(summary = "매물관리-두번째 페이지네이션")
    @GetMapping("/my-properties/table")
    public PagedDto<MyPropertyTableResponse> getTablesByOffset(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long adminId = customUserDetails.getUserId();
        return propertyService.getTables(page, size, adminId);
    }

    /**
     * 매물을 삭제하는 메소드
     *
     * @param propertyId
     * @param customUserDetails
     */
    @Operation(summary = "매물삭제")
    @GetMapping("/properties/{propertyId}")
    public void deleteProperty(
            @PathVariable Long propertyId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long adminId = customUserDetails.getUserId();
        propertyService.deleteProperty(propertyId, adminId);
    }

    @PatchMapping("/properties/{propertyId}")
    public void updateProperty(
            @PathVariable Long propertyId,
            @Valid @RequestBody PropertyUpdateRequest requestDTO,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long adminId = customUserDetails.getUserId();
        Property updatedProperty = propertyService.updateProperty(propertyId, requestDTO, adminId);
        System.out.println("성공");
    }
}
