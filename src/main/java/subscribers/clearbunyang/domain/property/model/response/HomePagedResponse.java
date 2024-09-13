package subscribers.clearbunyang.domain.property.model.response;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.global.model.PagedDto;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomePagedResponse {

    private String image1;
    private String image2;
    private String image3;
    private PagedDto<List<HomePropertiesResponse>> content;
}
