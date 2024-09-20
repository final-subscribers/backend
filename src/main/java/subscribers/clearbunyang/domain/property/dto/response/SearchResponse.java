package subscribers.clearbunyang.domain.property.dto.response;


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

    private int totalProperties;
    private List<PropertySummaryResponse> propertySearchResponses;

    public static SearchResponse toDto(
            int totalProperties, List<PropertySummaryResponse> propertySearchResponses) {
        return SearchResponse.builder()
                .totalProperties(totalProperties)
                .propertySearchResponses(propertySearchResponses)
                .build();
    }
}
