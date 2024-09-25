package subscribers.clearbunyang.domain.consultation.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subscribers.clearbunyang.domain.consultation.dto.adminPropertyConsultation.response.SideBarListResponse;
import subscribers.clearbunyang.domain.consultation.service.AdminPropertyConsultationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/properties/sidebar")
@Tag(name = "고객관리- 사이드바")
public class AdminSidebarController {

    private final AdminPropertyConsultationService adminPropertyConsultationService;

    @Operation(summary = "사이드바 리스트만 조회")
    @GetMapping
    public SideBarListResponse getSideBarListResponse() {
        return adminPropertyConsultationService.getSideBarList();
    }
}
