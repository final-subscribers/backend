package subscribers.clearbunyang.domain.consultation.model.dashboard;


import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PropertyInquiryDetailsDTO {
    String propertyName;
    Integer pending = 0;
    Integer completed = 0;
    Integer all;
    Integer phone = 0;
    Integer channel = 0;
    Integer lms = 0;

    @Setter List<LocalDateTime> timeStamps = List.of();

    public PropertyInquiryDetailsDTO(
            String propertyName,
            Integer pending,
            Integer completed,
            Integer phone,
            Integer channel,
            Integer lms) {
        this.propertyName = propertyName;
        this.pending = pending;
        this.completed = completed;
        this.all = pending + completed;
        this.phone = phone;
        this.channel = channel;
        this.lms = lms;
    }

    public PropertyInquiryDetailsDTO(Integer all) {
        this.all = 0;
    }
}
