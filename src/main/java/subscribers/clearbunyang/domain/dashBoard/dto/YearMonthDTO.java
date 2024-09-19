package subscribers.clearbunyang.domain.dashBoard.dto;


import java.time.LocalDate;
import org.jetbrains.annotations.NotNull;

public record YearMonthDTO(int year, int month) implements Comparable<YearMonthDTO> {

    public static YearMonthDTO of(int year, int month) {
        return new YearMonthDTO(year, month);
    }

    public static YearMonthDTO from(LocalDate date) {
        int year = date.getYear();
        int month = date.getMonthValue();
        return new YearMonthDTO(year, month);
    }

    @Override
    public int compareTo(@NotNull YearMonthDTO other) {
        if (this.year != other.year) {
            return Integer.compare(this.year, other.year);
        } else {
            return Integer.compare(this.month, other.month);
        }
    }
}
