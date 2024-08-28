package subscribers.clearbunyang.domain.property.model;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeywordsRequestDTO {
    private List<KeywordDTO> keywords;
}
