package subscribers.clearbunyang.domain.consultation.model.dashboard.response;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.domain.consultation.model.dashboard.ConsultationDateStatsDTO;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class CardWeekProgressResponse {
    Integer completed;
    Integer all;

    public static CardWeekProgressResponse fromDTO(ConsultationDateStatsDTO dto) {
        return CardWeekProgressResponse.builder()
                .completed(dto.getCompleted())
                .all(dto.getAll())
                .build();
    }
}
