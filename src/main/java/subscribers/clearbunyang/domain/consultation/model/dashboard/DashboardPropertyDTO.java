package subscribers.clearbunyang.domain.consultation.model.dashboard;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.domain.consultation.entity.enums.Period;
import subscribers.clearbunyang.domain.consultation.entity.enums.Status;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DashboardPropertyDTO {
    String propertyName;
    Status status;
    Period period;
    Integer pending;
    Integer completed;
    Integer all;
    Integer phone;
    Integer channel;
    Integer lms;
    List<ConsultationProgressDTO> eachPeriod;
}
