package subscribers.clearbunyang.domain.consultation.model.response;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ConsultPendingListResponse {

    private List<ConsultPendingSummaryResponse> consultPendingSummaries;

    public static ConsultPendingListResponse toDto(
            List<ConsultPendingSummaryResponse> responseList) {
        return ConsultPendingListResponse.builder().consultPendingSummaries(responseList).build();
    }
}
