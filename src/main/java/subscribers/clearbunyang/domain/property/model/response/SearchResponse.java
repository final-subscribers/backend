package subscribers.clearbunyang.domain.property.model.response;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchResponse {

    private List<PropertySearchResponse> propertySearchResponses;

    public static SearchResponse toDto(List<PropertySearchResponse> propertySearchResponses) {
        return SearchResponse.builder().propertySearchResponses(propertySearchResponses).build();
    }
}
