package subscribers.clearbunyang.domain.consultation.model.response;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SideBarListResponse {

    private List<SideBarPendingResponse> sideBarPendingResponseList;

    private List<SideBarCompletedResponse> sideBarCompletedResponseList;

    private SideBarSelectedPropertyResponse sideBarSelectedPropertyResponse;

    public static SideBarListResponse toDto(
            List<SideBarPendingResponse> pendingList,
            List<SideBarCompletedResponse> completedList,
            SideBarSelectedPropertyResponse selectedProperty) {
        return SideBarListResponse.builder()
                .sideBarPendingResponseList(pendingList)
                .sideBarCompletedResponseList(completedList)
                .sideBarSelectedPropertyResponse(selectedProperty)
                .build();
    }
}
