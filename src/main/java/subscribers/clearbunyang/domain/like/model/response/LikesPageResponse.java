package subscribers.clearbunyang.domain.like.model.response;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikesPageResponse {

    private long favoriteNumber;
    private int totalPages;
    private int pageSize;
    private int currentPage;
    private List<LikesPropertyResponse> properties;

    public static LikesPageResponse fromPage(
            Page<LikesPropertyResponse> propertyPage, int pageSize, int currentPage) {
        return LikesPageResponse.builder()
                .favoriteNumber(propertyPage.getTotalElements())
                .totalPages(propertyPage.getTotalPages())
                .pageSize(pageSize)
                .currentPage(currentPage)
                .properties(propertyPage.getContent())
                .build();
    }
}
