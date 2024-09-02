package subscribers.clearbunyang.domain.consultation.model.dashboard;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PropertyInquiryStatusDTO {
    String propertyName;
    Integer pending = 0;
    Integer completed = 0;
    Integer all;

    public PropertyInquiryStatusDTO(Integer pending, Integer completed) {
        this.pending = pending;
        this.completed = completed;
        this.all = pending + completed;
    }

    public PropertyInquiryStatusDTO(String propertyName, Integer pending, Integer completed) {
        this.propertyName = propertyName;
        this.pending = pending;
        this.completed = completed;
        this.all = pending + completed;
    }
}
