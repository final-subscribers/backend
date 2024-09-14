package subscribers.clearbunyang.domain.consultation.model.dashboard;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PropertyInquiryDetailsDTO {
    Integer pending = 0;
    Integer completed = 0;
    Integer all;
    Integer phone = 0;
    Integer channel = 0;
    Integer lms = 0;

    public PropertyInquiryDetailsDTO(
            Integer pending, Integer completed, Integer phone, Integer channel, Integer lms) {
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
