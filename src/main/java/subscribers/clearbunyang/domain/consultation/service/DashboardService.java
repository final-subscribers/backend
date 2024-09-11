/*
package subscribers.clearbunyang.domain.consultation.service;


@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardRepository dashboardRepository;

    public DashboardInitDTO getDashboard(Long adminId) {
        List<PropertySelectDTO> selects = dashboardRepository.findDropdownSelects(adminId, OPEN);
        PropertyInquiryStatusDTO todayStats = dashboardRepository.findTodayStats(adminId);
        List<PropertyInquiryStatusDTO> properties =
                dashboardRepository.findStatsOrderByCountDesc(adminId);
        List<ConsultationDateStatsDTO> totalNumberByWeek =
                dashboardRepository.findTotalNumberByWeek(adminId);
        PropertyInquiryDetailsDTO situation =
                dashboardRepository.findPropertyInquiryDetails(
                        selects.get(0).getPropertyId(), LocalDate.now(), LocalDate.now());

        return DashboardInitDTO.builder()
                .today(todayStats)
                .totalNumberByWeek(totalNumberByWeek)
                .highestConsultation(properties.get(0))
                .lowestConsultation(properties.get(properties.size() - 1))
                .dropdown(selects)
                .situation(situation)
                .build();
    }

    public PagedDto<PropertiesInquiryStatsDTO> getPropertiesInquiryStats(
            Long adminId, Pageable pageable) {
        Page<PropertiesInquiryStatsDTO> propertiesInquiryStats =
                dashboardRepository.findPropertiesInquiryStats(adminId, pageable);

        return PageToPagedDtoConverter.convertToPagedDto(propertiesInquiryStats);
    }

    public PropertyInquiryDetailsDTO getPropertyInquiryDetails(
            Long propertyId, LocalDate start, LocalDate end) {
        return dashboardRepository.findPropertyInquiryDetails(propertyId, start, end);
    }
}
*/
