package subscribers.clearbunyang.domain.consultation.model.dashboard.response;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.domain.consultation.model.dashboard.PropertyInquiryStatusDTO;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PropertyInquiryStatusResponse {
    Integer pending;
    Integer all;

    public static PropertyInquiryStatusResponse fromDTO(PropertyInquiryStatusDTO dto) {
        return PropertyInquiryStatusResponse.builder()
                .pending(dto.getPending())
                .all(dto.getAll())
                .build();
    }
}
