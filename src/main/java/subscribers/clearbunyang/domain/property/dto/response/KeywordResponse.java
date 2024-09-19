package subscribers.clearbunyang.domain.property.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordName;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeywordResponse {
    private KeywordName name;

    private KeywordType type; // BENEFIT/INFRA

    private Boolean searchEnabled;

    private Object input;
}
