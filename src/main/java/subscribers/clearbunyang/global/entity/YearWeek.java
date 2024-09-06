package subscribers.clearbunyang.global.entity;


import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;
import lombok.Getter;

@Getter
public class YearWeek implements Comparable<YearWeek> {
    private final int year;
    private final int week;

    public YearWeek(int year, int week) {
        this.year = year;
        this.week = week;
    }

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        YearWeek yearWeek = (YearWeek) o;

        if (year != yearWeek.year) return false;
        return week == yearWeek.week;
    }

    @Override
    public int hashCode() {
        int result = year;
        result = 31 * result + week;
        return result;
    }

    @Override
    public String toString() {
        return "YearWeek{" + "year=" + year + ", week=" + week + '}';
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
