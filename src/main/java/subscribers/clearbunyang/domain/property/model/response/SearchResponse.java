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

    private List<PropertySummaryResponse> propertyResponseList;

    public static SearchResponse toDto(List<PropertySummaryResponse> propertyResponseList) {
        return SearchResponse.builder().propertyResponseList(propertyResponseList).build();
    }
}
