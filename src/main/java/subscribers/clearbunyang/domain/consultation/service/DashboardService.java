package subscribers.clearbunyang.domain.consultation.service;


import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import subscribers.clearbunyang.domain.consultation.model.dashboard.ConsultationDateStatsDTO;
import subscribers.clearbunyang.domain.consultation.model.dashboard.DashboardInitDTO;
import subscribers.clearbunyang.domain.consultation.model.dashboard.PropertiesInquiryStatsDTO;
import subscribers.clearbunyang.domain.consultation.model.dashboard.PropertyInquiryDetailsDTO;
import subscribers.clearbunyang.domain.consultation.model.dashboard.PropertyInquiryStatusDTO;
import subscribers.clearbunyang.domain.consultation.model.dashboard.PropertySelectDTO;
import subscribers.clearbunyang.domain.consultation.repository.dashboard.DashboardRepository;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardRepository dashboardRepository;

    public DashboardInitDTO getDashboard(Long adminId) {
        List<PropertySelectDTO> selects = dashboardRepository.findDropdownSelects(adminId);
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

    public Page<PropertiesInquiryStatsDTO> getPropertiesInquiryStats(
            Long adminId, Pageable pageable) {
        return dashboardRepository.findPropertiesInquiryStats(adminId, pageable);
    }

    public PropertyInquiryDetailsDTO getPropertyInquiryDetails(
            Long propertyId, LocalDate start, LocalDate end) {
        return dashboardRepository.findPropertyInquiryDetails(propertyId, start, end);
    }
}
