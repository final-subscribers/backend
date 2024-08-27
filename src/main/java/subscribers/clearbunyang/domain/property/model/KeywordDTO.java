package subscribers.clearbunyang.domain.property.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordTier;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeywordDTO {
    private KeywordTier tier;
    private String type; // 혜택줍줍/주변핵심체크
    private String name;
    private String value;
}
