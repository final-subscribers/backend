package subscribers.clearbunyang.domain.dashBoard.dto;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PropertyGraphRequirementsDTO {
    Integer interval;
    Integer year;
    Integer month;
    Integer day;
    Integer pending = 0;
    Integer completed = 0;
    Integer all;

    public PropertyGraphRequirementsDTO(Integer pending, Integer completed) {
        this.pending = pending;
        this.completed = completed;
        this.all = pending + completed;
    }

    public PropertyGraphRequirementsDTO(Integer interval, Integer pending, Integer completed) {
        this.interval = interval;
        this.pending = pending;
        this.completed = completed;
        this.all = pending + completed;
    }

    public PropertyGraphRequirementsDTO(
            Integer year, Integer month, Integer day, Integer pending, Integer completed) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.pending = pending;
        this.completed = completed;
        this.all = pending + completed;
    }

    public PropertyGraphRequirementsDTO(
            Integer year, Integer month, Integer pending, Integer completed) {
        this.year = year;
        this.month = month;
        this.pending = pending;
        this.completed = completed;
        this.all = pending + completed;
    }
}
