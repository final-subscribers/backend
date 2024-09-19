package subscribers.clearbunyang.domain.dashBoard.dto.response;


import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class DropdownSelectsResponse {
    List<PropertySelectResponse> openList;
    List<PropertySelectResponse> closedList;
}
