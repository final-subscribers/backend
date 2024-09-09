package subscribers.clearbunyang.domain.property.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import subscribers.clearbunyang.domain.property.model.request.PropertyRequestDTO;
import subscribers.clearbunyang.domain.property.service.PropertyService;
import subscribers.clearbunyang.global.security.details.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/properties")
public class AdminPropertyController {

    private final PropertyService propertyService;

    /**
     * 물건을 저장하는 메소드
     *
     * @param requestDTO
     * @param CustomUserDetails jwt 토큰으로 받은 user 객체
     * @return
     */
    @PostMapping("")
    public void addProperty(
            @Valid @RequestBody PropertyRequestDTO requestDTO,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        propertyService.saveProperty(requestDTO, customUserDetails.getUserId());
    }
}
