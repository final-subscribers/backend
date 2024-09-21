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
                                "https://i0.wp.com/nurproperties.com/wp-content/uploads/2019/12/tsdlt00004.jpg?fit=960%2C540&ssl=1",
                                "https://i0.wp.com/www.homesinmalta.com/wp-content/uploads/2023/02/facade-1.jpg?fit=960%2C540&ssl=1",
                                "https://image.slidesdocs.com/responsive-images/background/city-tall-building-buildings-powerpoint-background_2bd46ecff0__960_540.jpg"))
                .properties(homeProperties)
                .build();
    }
}
