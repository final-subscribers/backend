package subscribers.clearbunyang.domain.dashBoard.dto.response;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.domain.dashBoard.dto.PropertyGraphRequirementsDTO;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class GraphRequirementsResponse {
    Integer pending;
    Integer completed;
    Integer all;

    public static GraphRequirementsResponse of(PropertyGraphRequirementsDTO dto) {
        return GraphRequirementsResponse.builder()
                .pending(dto.getPending())
                .completed(dto.getCompleted())
                .all(dto.getAll())
                .build();
    }
}
