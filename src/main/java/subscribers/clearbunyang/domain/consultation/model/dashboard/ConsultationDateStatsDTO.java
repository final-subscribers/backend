package subscribers.clearbunyang.domain.consultation.model.dashboard;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ConsultationDateStatsDTO {
    Integer year;
    Integer week;
    Integer pending = 0;
    Integer completed = 0;
    Integer all;

    public ConsultationDateStatsDTO(
            Integer year, Integer week, Integer pending, Integer completed) {
        this.year = year;
        this.week = week;
        this.pending = pending;
        this.completed = completed;
        this.all = pending + completed;
    }
}
