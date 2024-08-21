package subscribers.clearbunyang.domain.consultation.entity.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;

public enum Status {
    PENDING,
    COMPLETED;

    @JsonCreator
    public static Status fromString(String value) {
        return Stream.of(Status.values())
                .filter(status -> status.toString().equals(value.toUpperCase()))
                .findAny()
                .orElse(null);
    }
}
