package subscribers.clearbunyang.domain.dashBoard.dto;


import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

public record YearWeekDTO(int year, int week) implements Comparable<YearWeekDTO> {

    public static YearWeekDTO of(int year, int week) {
        return new YearWeekDTO(year, week);
    }

    public static YearWeekDTO from(LocalDate date) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int year = date.getYear();
        int week = date.get(weekFields.weekOfWeekBasedYear());
        return new YearWeekDTO(year, week);
    }

    public LocalDate atDay(int dayOfWeek) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return LocalDate.ofYearDay(year, 1)
                .with(weekFields.weekOfWeekBasedYear(), week)
                .with(weekFields.dayOfWeek(), dayOfWeek);
    }

    @Override
    public int compareTo(YearWeekDTO other) {
        if (this.year != other.year) {
            return Integer.compare(this.year, other.year);
        } else {
            return Integer.compare(this.week, other.week);
        }
    }
}
