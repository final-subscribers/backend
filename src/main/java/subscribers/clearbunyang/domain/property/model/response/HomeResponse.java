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
public class HomeResponse {

    private List<String> homeImagesUrl;
    private List<HomePropertiesResponse> properties;

    public static HomeResponse toDto(List<HomePropertiesResponse> homeProperties) {
        return HomeResponse.builder()
                .homeImagesUrl(
                        List.of(
                                "https://down.humoruniv.com//hwiparambbs/data/editor/pdswait/e_s941967001_54424d83ad875aebff802c182e0bb4d96e88a6fb.jpg",
                                "https://down.humoruniv.com//hwiparambbs/data/editor/pdswait/e_s941967002_b83b74ed967105c20c99d8686749ba9efc5ef607.jpg",
                                "https://down.humoruniv.com//hwiparambbs/data/editor/pdswait/e_s941967004_441b9e09e1edf1d1b1dd412ab24c429936bfa41c.jpg"))
                .properties(homeProperties)
                .build();
    }
}
