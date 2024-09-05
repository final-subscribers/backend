package subscribers.clearbunyang.domain.consultation.model.dashboard;


import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class DashboardInitDTO {
    PropertyInquiryStatusDTO today;
    List<ConsultationDateStatsDTO> lastFiveWeeks;
    PropertyInquiryStatusDTO highestConsultation;
    PropertyInquiryStatusDTO lowestConsultation;
    List<PropertySelectDTO> properties;
    PropertyInquiryDetailsDTO situation;
}
