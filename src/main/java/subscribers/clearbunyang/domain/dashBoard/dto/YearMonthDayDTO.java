package subscribers.clearbunyang.domain.dashBoard.dto;


import java.time.LocalDate;
import org.jetbrains.annotations.NotNull;

public record YearMonthDayDTO(int year, int month, int day) implements Comparable<YearMonthDayDTO> {

    public static YearMonthDayDTO of(int year, int month, int day) {
        return new YearMonthDayDTO(year, month, day);
    }

    public static YearMonthDayDTO from(LocalDate date) {
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        return new YearMonthDayDTO(year, month, day);
    }

    @Override
    public int compareTo(@NotNull YearMonthDayDTO other) {
        if (this.year != other.year) {
            return Integer.compare(this.year, other.year);
        } else if (this.month != other.month) {
            return Integer.compare(this.month, other.month);
        } else {
            return Integer.compare(this.day, other.day);
        }
    }
}
