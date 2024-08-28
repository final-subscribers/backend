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
public class ConsultantListResponse {

    private List<ConsultantResponse> consultantResponses;

    public static ConsultantListResponse toDto(List<ConsultantResponse> consultantResponses) {
        return ConsultantListResponse.builder().consultantResponses(consultantResponses).build();
    }
}
