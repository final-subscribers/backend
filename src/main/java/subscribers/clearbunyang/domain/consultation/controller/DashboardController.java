package subscribers.clearbunyang.domain.consultation.controller;


import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subscribers.clearbunyang.domain.consultation.model.dashboard.PropertyInquiryDetailsDTO;
import subscribers.clearbunyang.domain.consultation.model.dashboard.response.CardComponentResponse;
import subscribers.clearbunyang.domain.consultation.model.dashboard.response.PropertyInquiryStatusResponse;
import subscribers.clearbunyang.domain.consultation.service.DashboardService;
import subscribers.clearbunyang.global.model.PagedDto;
import subscribers.clearbunyang.global.security.details.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("dashboard/cards")
    public CardComponentResponse getCards(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return dashboardService.getCards(customUserDetails.getUserId());
    }

    @GetMapping("dashboard/properties")
    public PagedDto<PropertyInquiryStatusResponse> getDashboardProperties(
            @PageableDefault(size = 5) Pageable pageable,
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
