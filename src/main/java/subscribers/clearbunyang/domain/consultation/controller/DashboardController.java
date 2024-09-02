package subscribers.clearbunyang.domain.consultation.controller;


import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subscribers.clearbunyang.domain.consultation.model.dashboard.DashboardInitDTO;
import subscribers.clearbunyang.domain.consultation.model.dashboard.PropertiesInquiryStatsDTO;
import subscribers.clearbunyang.domain.consultation.model.dashboard.PropertyInquiryDetailsDTO;
import subscribers.clearbunyang.domain.consultation.service.DashboardService;
import subscribers.clearbunyang.global.security.details.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("dashboard")
    public DashboardInitDTO getDashboard() {
        return null;
    }

    @GetMapping("dashboard/properties")
    public Page<PropertiesInquiryStatsDTO> getDashboardProperties(
            @PageableDefault Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return dashboardService.getPropertiesInquiryStats(customUserDetails.getUserId(), pageable);
    }

    @GetMapping("/properties/{property_id}")
    public PropertyInquiryDetailsDTO getDashboardProperty(
            @PathVariable(name = "property_id") String propertyId,
            @RequestParam(name = "start") LocalDate start,
            @RequestParam(name = "end") LocalDate end) {
        return dashboardService.getPropertyInquiryDetails(Long.valueOf(propertyId), start, end);
    }
}
