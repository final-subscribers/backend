package subscribers.clearbunyang.domain.dashBoard.dto;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PropertyInquiryStatusDTO {
    @Setter Long propertyId;
    String propertyName;
    Integer pending = 0;
    Integer completed = 0;
    Integer all;

    public PropertyInquiryStatusDTO(Integer pending, Integer completed) {
        this.pending = pending;
        this.completed = completed;
        this.all = pending + completed;
    }

    public PropertyInquiryStatusDTO(Long propertyId, Integer pending, Integer completed) {
        this.propertyId = propertyId;
        this.pending = pending;
        this.completed = completed;
        this.all = pending + completed;
    }

    public PropertyInquiryStatusDTO(
            Long propertyId, String propertyName, Integer pending, Integer completed) {
        this.propertyId = propertyId;
        this.propertyName = propertyName;
        this.pending = pending;
        this.completed = completed;
        this.all = pending + completed;
    }
}
