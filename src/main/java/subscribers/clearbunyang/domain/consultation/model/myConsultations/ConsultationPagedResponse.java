package subscribers.clearbunyang.domain.consultation.model.myConsultations;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.global.model.PagedDto;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultationPagedResponse {

    private int totalCount;
    private PagedDto<List<MyPendingConsultationsResponse>> pagedData;
}
