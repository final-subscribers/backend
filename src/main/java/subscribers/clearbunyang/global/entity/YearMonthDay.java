package subscribers.clearbunyang.global.entity;


import java.time.LocalDate;
import org.jetbrains.annotations.NotNull;

public record YearMonthDay(int year, int month, int day) implements Comparable<YearMonthDay> {

    public static YearMonthDay of(int year, int month, int day) {
        return new YearMonthDay(year, month, day);
    }

    public static YearMonthDay from(LocalDate date) {
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        return new YearMonthDay(year, month, day);
    }

    @Override
    public int compareTo(@NotNull YearMonthDay other) {
        if (this.year != other.year) {
            return Integer.compare(this.year, other.year);
        } else if (this.month != other.month) {
            return Integer.compare(this.month, other.month);
        } else {
            return Integer.compare(this.day, other.day);
        }
    }
}
