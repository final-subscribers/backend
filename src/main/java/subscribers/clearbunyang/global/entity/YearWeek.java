package subscribers.clearbunyang.global.entity;


import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

public record YearWeek(int year, int week) implements Comparable<YearWeek> {

    public static YearWeek of(int year, int week) {
        return new YearWeek(year, week);
    }

    public static YearWeek from(LocalDate date) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int year = date.getYear();
        int week = date.get(weekFields.weekOfWeekBasedYear());
        return new YearWeek(year, week);
    }

    public LocalDate atDay(int dayOfWeek) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return LocalDate.ofYearDay(year, 1)
                .with(weekFields.weekOfWeekBasedYear(), week)
                .with(weekFields.dayOfWeek(), dayOfWeek);
    }

    @Override
    public int compareTo(YearWeek other) {
        if (this.year != other.year) {
            return Integer.compare(this.year, other.year);
        } else {
            return Integer.compare(this.week, other.week);
        }
    }
}
