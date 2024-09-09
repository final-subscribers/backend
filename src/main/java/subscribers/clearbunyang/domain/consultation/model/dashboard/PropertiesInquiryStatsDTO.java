package subscribers.clearbunyang.domain.consultation.model.dashboard;


import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PropertiesInquiryStatsDTO {
    LocalDateTime startTime;
    LocalDateTime endDate;
    Long propertyId;
    String propertyName;
    Integer pending = 0;
    Integer completed = 0;
    Integer all = 0;

    public PropertiesInquiryStatsDTO(
            Long propertyId, String propertyName, Integer pending, Integer completed) {
        this.propertyId = propertyId;
        this.propertyName = propertyName;
        this.pending = pending;
        this.completed = completed;
    }

    public PropertiesInquiryStatsDTO(Long propertyId, String propertyName) {
        this.propertyId = propertyId;
        this.propertyName = propertyName;
    }
}
