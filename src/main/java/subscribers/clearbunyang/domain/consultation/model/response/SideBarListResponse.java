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

    private List<PendingResponse> pendingResponseList;

    private List<CompletedResponse> completedResponseList;

    private SelectedPropertyResponse selectedPropertyResponse;
}
