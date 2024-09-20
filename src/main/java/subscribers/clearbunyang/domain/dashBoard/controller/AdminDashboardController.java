package subscribers.clearbunyang.domain.dashBoard.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subscribers.clearbunyang.domain.dashBoard.dto.response.CardComponentResponse;
import subscribers.clearbunyang.domain.dashBoard.dto.response.DropdownSelectsResponse;
import subscribers.clearbunyang.domain.dashBoard.dto.response.PropertiesInquiryStatusResponse;
import subscribers.clearbunyang.domain.dashBoard.dto.response.PropertyInquiryDetailsResponse;
import subscribers.clearbunyang.domain.dashBoard.entity.enums.GraphInterval;
import subscribers.clearbunyang.domain.dashBoard.service.AdminDashboardService;
import subscribers.clearbunyang.global.security.details.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/")
@Tag(name = "대시보드 조회", description = "admin이 받은 신청들에 대한 대시보드 조회")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    @GetMapping("dashboard/dropdown-selects")
    public DropdownSelectsResponse getDropdownSelects(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return adminDashboardService.getDropdownSelects(customUserDetails.getUserId());
    }

    @GetMapping("dashboard/cards")
    public CardComponentResponse getCards(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return adminDashboardService.getCards(customUserDetails.getUserId());
    }

    @GetMapping("dashboard/properties")
    public PropertiesInquiryStatusResponse getDashboardProperties(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "5") int size,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return adminDashboardService.getPropertiesInquiryStats(
                customUserDetails.getUserId(), page, size);
    }

    @GetMapping("/dashboard/properties/{property_id}")
    public PropertyInquiryDetailsResponse getDashboardProperty(
            @PathVariable(name = "property_id") String propertyId,
            @RequestParam(name = "end") LocalDate end,
            @RequestParam(name = "graphInterval") GraphInterval graphInterval) {
        return adminDashboardService.getPropertyInquiryDetails(
                Long.valueOf(propertyId), end, graphInterval);
    }
}
