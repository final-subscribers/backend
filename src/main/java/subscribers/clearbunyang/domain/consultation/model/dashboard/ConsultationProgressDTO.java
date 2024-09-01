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
    LocalDateTime startTime;
    LocalDateTime endDate;
    String periodLabel;
    Long propertyId;
    String propertyName;
    Integer pending;
    Integer completed;
    Integer all;

    public ConsultationProgressDTO(
            Long propertyId, String propertyName, Integer pending, Integer completed) {
        this.propertyId = propertyId;
        this.propertyName = propertyName;
        this.pending = pending;
        this.completed = completed;
    }

    public ConsultationProgressDTO(Long propertyId, String propertyName) {
        this.propertyId = propertyId;
        this.propertyName = propertyName;
    }
}
