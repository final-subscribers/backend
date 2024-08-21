package subscribers.clearbunyang.domain.consultation.model.response;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ConsultPendingResponse {

    private List<ConsultPendingSummaryResponse> consultPendingSummaries;

    public static ConsultPendingResponse toDto(List<ConsultPendingSummaryResponse> responseList) {
        return ConsultPendingResponse.builder().consultPendingSummaries(responseList).build();
    }
}
