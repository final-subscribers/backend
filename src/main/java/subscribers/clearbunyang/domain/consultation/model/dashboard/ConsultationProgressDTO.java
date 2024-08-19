package subscribers.clearbunyang.domain.consultation.model.dashboard;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
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
public class ConsultationProgressDTO {
    LocalDateTime startDate;
    LocalDateTime endDate;
    String periodLabel;
    String propertyName;
    Integer pending;
    Integer completed;
    Integer all;
}
