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
public class HomeResponse {

    private List<String> homeImagesUrl;
    private List<PropertySummaryResponse> properties;

    public static HomeResponse toDto(List<PropertySummaryResponse> homeProperties) {
        return HomeResponse.builder()
                .homeImagesUrl(
                        List.of(
                                "https://cdn.pixabay.com/photo/2015/11/06/11/48/multi-family-home-1026490_1280.jpg",
                                "https://cdn.pixabay.com/photo/2017/03/27/15/17/apartment-2179337_1280.jpg",
                                "https://cdn.pixabay.com/photo/2022/09/16/17/07/city-7459162_1280.jpg"))
                .properties(homeProperties)
                .build();
    }
}
