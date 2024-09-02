package subscribers.clearbunyang.domain.consultation.service;


import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import subscribers.clearbunyang.domain.consultation.model.dashboard.PropertiesInquiryStatsDTO;
import subscribers.clearbunyang.domain.consultation.model.dashboard.PropertyInquiryDetailsDTO;
import subscribers.clearbunyang.domain.consultation.repository.dashboard.DashboardRepository;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final DashboardRepository dashboardRepository;

    public Page<PropertiesInquiryStatsDTO> getPropertiesInquiryStats(
            Long userId, Pageable pageable) {
        return dashboardRepository.findPropertiesInquiryStats(userId, pageable);
    }

    public PropertyInquiryDetailsDTO getPropertyInquiryDetails(
            Long propertyId, LocalDate start, LocalDate end) {
        return dashboardRepository.findPropertyInquiryDetails(propertyId, start, end);
    }
}
