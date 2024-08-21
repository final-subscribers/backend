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
public class ConsultCompletedResponse {

    private List<ConsultCompletedSummaryResponse> consultCompletedSummaries;

    public static ConsultCompletedResponse toDto(
            List<ConsultCompletedSummaryResponse> responseList) {
        return ConsultCompletedResponse.builder().consultCompletedSummaries(responseList).build();
    }
}
