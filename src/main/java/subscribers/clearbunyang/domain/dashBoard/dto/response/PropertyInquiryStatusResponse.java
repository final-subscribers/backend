package subscribers.clearbunyang.domain.dashBoard.dto.response;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.domain.dashBoard.dto.PropertyInquiryStatusDTO;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PropertyInquiryStatusResponse {

    String propertyName;
    Integer pending;
    Integer all;

    public static PropertyInquiryStatusResponse fromDTO(PropertyInquiryStatusDTO dto) {
        return PropertyInquiryStatusResponse.builder()
                .propertyName(dto.getPropertyName())
                .pending(dto.getPending())
                .all(dto.getAll())
                .build();
    }
}
