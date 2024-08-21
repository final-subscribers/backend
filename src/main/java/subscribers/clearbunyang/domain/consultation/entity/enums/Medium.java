package subscribers.clearbunyang.domain.consultation.entity.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum Medium {
    PHONE,
    LMS,
    NONE;

    @JsonCreator
    public static Medium fromString(String value) {
        return Stream.of(Medium.values())
                .filter(status -> status.toString().equals(value.toUpperCase()))
                .findAny()
                .orElse(null);
    }
}
