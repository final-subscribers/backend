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
public class DashboardResponseDTO {
    ConsultationProgressDTO today;
    ConsultationProgressDTO thisWeek;
    ConsultationProgressDTO lastWeek;
    List<ConsultationProgressDTO> lastFiveWeeks;
    ConsultationProgressDTO highestConsultation;
    ConsultationProgressDTO lowestConsultation;
    Integer allProperties;
    List<ConsultationProgressDTO> properties;
    List<String> statusProperties;
    DashboardPropertyDTO situation;
}
