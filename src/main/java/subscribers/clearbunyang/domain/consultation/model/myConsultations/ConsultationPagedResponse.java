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
public class ConsultationPagedResponse {

    private int totalCount;
    private List<MyPendingConsultationsResponse> pagedData;
}
