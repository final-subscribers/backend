package subscribers.clearbunyang.domain.property.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import subscribers.clearbunyang.domain.property.dto.request.MemberConsultationRequest;
import subscribers.clearbunyang.domain.property.dto.response.PropertyDetailsResponse;
import subscribers.clearbunyang.domain.property.service.PropertyService;
import subscribers.clearbunyang.global.security.details.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "매물 상세페이지", description = "매물 상세페이지 조회/유저의 상담 등록")
public class CommonPropertyController {

    private final PropertyService propertyService;

    /**
     * 비회원이 상담을 등록하는 메소드
     *
     * @param propertyId
     * @param requestDTO
     */
    @Operation(summary = "비회원의 상담 등록")
    @PostMapping("/common/properties/{propertyId}/consultation")
    public void addConsultation1(
            @PathVariable Long propertyId,
            @Valid @RequestBody MemberConsultationRequest requestDTO) {
        propertyService.saveConsultation(propertyId, requestDTO, null);
    }

    /**
     * 유저가 상담을 등록하는 메소드
     *
     * @param propertyId
     * @param requestDTO
     * @param customUserDetails
     */
    @Operation(summary = "유저의 상담 등록")
    @PostMapping("/member/properties/{propertyId}/consultation")
    public void addConsultation2(
            @PathVariable Long propertyId,
            @Valid @RequestBody MemberConsultationRequest requestDTO,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        propertyService.saveConsultation(propertyId, requestDTO, customUserDetails.getUserId());
    }

    /**
     * 매물 상세 정보를 가져오는 메소드
     *
     * @param propertyId
     * @param customUserDetails
     * @return
     */
    @Operation(summary = "매물 상세 조회")
    @GetMapping("/common/properties/{propertyId}")
    public PropertyDetailsResponse getProperty(
            @PathVariable Long propertyId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long memberId =
                (customUserDetails == null || customUserDetails.isInstanceOfAdmin())
                        ? null
                        : customUserDetails.getUserId();
        return propertyService.getPropertyDetails(propertyId, memberId);
    }
}
