package subscribers.clearbunyang.domain.dashBoard.repository;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import subscribers.clearbunyang.domain.dashBoard.dto.ConsultationDateStatsDTO;
import subscribers.clearbunyang.domain.dashBoard.dto.PropertyGraphRequirementsDTO;
import subscribers.clearbunyang.domain.dashBoard.dto.PropertyInquiryDetailsDTO;
import subscribers.clearbunyang.domain.dashBoard.dto.PropertyInquiryStatusDTO;
import subscribers.clearbunyang.domain.dashBoard.dto.PropertySelectDTO;
import subscribers.clearbunyang.domain.dashBoard.entity.enums.GraphInterval;
import subscribers.clearbunyang.domain.dashBoard.entity.enums.Phase;

public interface DashboardRepository {
    List<PropertySelectDTO> findDropdownSelects(Long adminId, Phase phase);

    PropertyInquiryStatusDTO findTodayStats(Long adminId);

    List<ConsultationDateStatsDTO> findTotalStatsByWeek(Long adminId);

    List<PropertyInquiryStatusDTO> findStatsOrderByCountDesc(Long adminId);

    Page<PropertyInquiryStatusDTO> findPropertiesInquiryStats(Long adminId, Pageable pageable);

    Optional<PropertyInquiryDetailsDTO> findPropertyInquiryDetails(
            Long propertyId, LocalDate end, GraphInterval graphInterval);

    List<PropertyGraphRequirementsDTO> findPropertyGraphDaily(Long propertyId, LocalDate end);

    List<PropertyGraphRequirementsDTO> findPropertyGraphWeekly(Long propertyId, LocalDate end);

    List<PropertyGraphRequirementsDTO> findPropertyGraphMonthly(Long propertyId, LocalDate end);
}
