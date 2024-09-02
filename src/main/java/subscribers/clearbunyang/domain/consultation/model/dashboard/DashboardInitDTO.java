package subscribers.clearbunyang.domain.consultation.model.dashboard;


import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DashboardInitDTO {
    PropertyInquiryStatusDTO today;
    PropertyInquiryStatusDTO thisWeek;
    PropertyInquiryStatusDTO lastWeek;
    List<PropertyInquiryStatusDTO> lastFiveWeeks;
    PropertyInquiryStatusDTO highestConsultation;
    PropertyInquiryStatusDTO lowestConsultation;
    Integer allProperties;
    List<PropertyInquiryStatusDTO> properties;
    List<String> statusProperties;
    PropertyInquiryDetailsDTO situation;
}
