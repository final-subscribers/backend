package subscribers.clearbunyang.domain.dashBoard.dto.response;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.domain.dashBoard.dto.PropertySelectDTO;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PropertySelectResponse {
    Long propertyId;
    String propertyName;

    public static PropertySelectResponse of(PropertySelectDTO dto) {
        return PropertySelectResponse.builder()
                .propertyId(dto.getPropertyId())
                .propertyName(dto.getPropertyName())
                .build();
    }
}
