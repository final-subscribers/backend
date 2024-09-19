package subscribers.clearbunyang.domain.dashBoard.dto;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
public class PropertySelectDTO {
    Long propertyId;
    String propertyName;
}
