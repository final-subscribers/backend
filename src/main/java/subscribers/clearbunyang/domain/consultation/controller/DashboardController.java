/*
package subscribers.clearbunyang.domain.consultation.controller;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("dashboard")
    public DashboardInitDTO getDashboard(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return dashboardService.getDashboard(customUserDetails.getUserId());
    }

    @GetMapping("dashboard/properties")
    public PagedDto<PropertiesInquiryStatsDTO> getDashboardProperties(
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
*/
