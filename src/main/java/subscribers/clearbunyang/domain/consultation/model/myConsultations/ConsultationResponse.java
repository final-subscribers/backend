package subscribers.clearbunyang.domain.consultation.model.myConsultations;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultationResponse {

    private int totalCount;
    private List<MyConsultationsResponse> myConsultations;

    public static ConsultationResponse toDto(
            int totalCount, List<MyConsultationsResponse> consultations) {
        return ConsultationResponse.builder()
                .totalCount(totalCount)
                .myConsultations(consultations)
                .build();
    }
}
