package subscribers.clearbunyang.domain.property.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import subscribers.clearbunyang.domain.property.model.request.ConsultationRequestDTO;
import subscribers.clearbunyang.domain.property.model.response.PropertyDetailsResponseDTO;
import subscribers.clearbunyang.domain.property.service.PropertyService;
import subscribers.clearbunyang.global.security.details.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/common/properties")
@Tag(name = "매물 상세페이지", description = "매물 상세페이지 조회/유저의 상담 등록")
public class CommonPropertyController {

    private final PropertyService propertyService;

    /**
     * 상담을 등록하는 메소드
     *
     * @param propertyId
     * @param requestDTO
     * @param customUserDetails 로그인한 사용자
     */
    @Operation(summary = "유저의 상담 등록")
    @PostMapping("{propertyId}/consultation")
    public void addConsultation(
            @PathVariable Long propertyId,
            @Valid @RequestBody ConsultationRequestDTO requestDTO,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long memberId;
        if (customUserDetails != null) {
            if (customUserDetails.isInstanceOfAdmin()) { // todo admin일때 걸러야 되는데 /common이 맞는지 고민좀...
                throw new AccessDeniedException("권한이 없습니다!"); // todo authorization 예외로 바꿔야되나
            } else { // member일때
                memberId = customUserDetails.getUserId();
            }
        } else { // 비로그인일떄
            memberId = null;
        }
        propertyService.saveConsultation(propertyId, requestDTO, memberId);
    }

    /**
     * 매물 상세 정보를 가져오는 메소드
     *
     * @param propertyId
     * @param customUserDetails
     * @return
     */
    @Operation(summary = "매물 상세 조회")
    @GetMapping("{propertyId}")
    public PropertyDetailsResponseDTO getProperty(
            @PathVariable Long propertyId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long memberId = customUserDetails != null ? customUserDetails.getUserId() : null;
        return propertyService.getPropertyDetails(propertyId, memberId);
    }
}
