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
public class CardCountDescResponse {
    String propertyName;
    Integer all;

    public static CardCountDescResponse fromDTO(PropertyInquiryStatusDTO dto) {
        return CardCountDescResponse.builder()
                .propertyName(dto.getPropertyName())
                .all(dto.getAll())
                .build();
    }
}
