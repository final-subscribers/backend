package subscribers.clearbunyang.domain.consultation.repository.dashboard;


import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import subscribers.clearbunyang.domain.consultation.entity.enums.dashboard.GraphInterval;
import subscribers.clearbunyang.domain.consultation.entity.enums.dashboard.Phase;
import subscribers.clearbunyang.domain.consultation.model.dashboard.ConsultationDateStatsDTO;
import subscribers.clearbunyang.domain.consultation.model.dashboard.PropertyGraphRequirementsDTO;
import subscribers.clearbunyang.domain.consultation.model.dashboard.PropertyInquiryDetailsDTO;
import subscribers.clearbunyang.domain.consultation.model.dashboard.PropertyInquiryStatusDTO;
import subscribers.clearbunyang.domain.consultation.model.dashboard.PropertySelectDTO;

public interface DashboardRepository {
    List<PropertySelectDTO> findDropdownSelects(Long adminId, Phase phase);

    PropertyInquiryStatusDTO findTodayStats(Long adminId);

    List<ConsultationDateStatsDTO> findTotalStatsByWeek(Long adminId);

    List<PropertyInquiryStatusDTO> findStatsOrderByCountDesc(Long adminId);

    Page<PropertyInquiryStatusDTO> findPropertiesInquiryStats(Long adminId, Pageable pageable);

    PropertyInquiryDetailsDTO findPropertyInquiryDetails(
            Long propertyId, LocalDate end, GraphInterval graphInterval);

    PropertyGraphRequirementsDTO findPropertyGraphRequirements();
}
