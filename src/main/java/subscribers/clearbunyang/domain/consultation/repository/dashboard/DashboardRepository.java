package subscribers.clearbunyang.domain.consultation.repository.dashboard;


import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import subscribers.clearbunyang.domain.consultation.model.dashboard.PropertiesInquiryStatsDTO;
import subscribers.clearbunyang.domain.consultation.model.dashboard.PropertyInquiryDetailsDTO;

public interface DashboardRepository {
    Page<PropertiesInquiryStatsDTO> findPropertiesInquiryStats(Long userId, Pageable pageable);

    PropertyInquiryDetailsDTO findPropertyInquiryDetails(
            Long propertyId, LocalDate start, LocalDate end);
}
