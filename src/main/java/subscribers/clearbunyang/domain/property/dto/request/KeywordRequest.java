package subscribers.clearbunyang.domain.property.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.*;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordName;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeywordRequest {
    @NotNull private KeywordName name;

    @NotNull private KeywordType type; // BENEFIT/INFRA

    @NotNull private Boolean searchEnabled;

    private Object input;
}
