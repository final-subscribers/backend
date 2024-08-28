package subscribers.clearbunyang.domain.consultation.entity.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;

public enum Tier {
    S,
    A,
    B,
    C,
    D;

    @JsonCreator
    public static Tier fromString(String value) {
        return Stream.of(Tier.values())
                .filter(tier -> tier.toString().equals(value.toUpperCase()))
                .findAny()
                .orElse(null);
    }

    public static boolean isValidStatus(String value) {
        for (Tier tier : Tier.values()) {
            if (tier.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
