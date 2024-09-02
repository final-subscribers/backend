package subscribers.clearbunyang.domain.consultation.model.dashboard;


import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashboardInitDTO {
    PropertiesInquiryStatsDTO today;
    PropertiesInquiryStatsDTO thisWeek;
    PropertiesInquiryStatsDTO lastWeek;
    List<PropertiesInquiryStatsDTO> lastFiveWeeks;
    PropertiesInquiryStatsDTO highestConsultation;
    PropertiesInquiryStatsDTO lowestConsultation;
    Integer allProperties;
    List<PropertiesInquiryStatsDTO> properties;
    List<String> statusProperties;
    PropertyInquiryDetailsDTO situation;
}
