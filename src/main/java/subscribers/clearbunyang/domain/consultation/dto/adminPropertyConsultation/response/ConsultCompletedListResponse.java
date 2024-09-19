package subscribers.clearbunyang.domain.consultation.dto.adminPropertyConsultation.response;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultCompletedListResponse {

    private List<ConsultCompletedSummaryResponse> consultCompletedSummaries;

    public static ConsultCompletedListResponse toDto(
            List<ConsultCompletedSummaryResponse> responseList) {
        return ConsultCompletedListResponse.builder()
                .consultCompletedSummaries(responseList)
                .build();
    }
}
