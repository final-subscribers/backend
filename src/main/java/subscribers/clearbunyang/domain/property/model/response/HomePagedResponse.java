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
public class HomePagedResponse {

    private String image1;
    private String image2;
    private String image3;
    private List<HomePropertiesResponse> homeProperties;

    public static HomePagedResponse toDto(
            String image1,
            String image2,
            String image3,
            List<HomePropertiesResponse> homeProperties) {
        return HomePagedResponse.builder()
                .image1(image1)
                .image2(image2)
                .image3(image3)
                .homeProperties(homeProperties)
                .build();
    }
}
