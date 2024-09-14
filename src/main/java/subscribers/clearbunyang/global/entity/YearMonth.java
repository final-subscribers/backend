package subscribers.clearbunyang.global.entity;


import java.time.LocalDate;
import org.jetbrains.annotations.NotNull;

public record YearMonth(int year, int month) implements Comparable<YearMonth> {

    public static YearMonth of(int year, int month) {
        return new YearMonth(year, month);
    }

    public static YearMonth from(LocalDate date) {
        int year = date.getYear();
        int month = date.getMonthValue();
        return new YearMonth(year, month);
    }

    @Override
    public int compareTo(@NotNull YearMonth other) {
        if (this.year != other.year) {
            return Integer.compare(this.year, other.year);
        } else {
            return Integer.compare(this.month, other.month);
        }
    }
}
