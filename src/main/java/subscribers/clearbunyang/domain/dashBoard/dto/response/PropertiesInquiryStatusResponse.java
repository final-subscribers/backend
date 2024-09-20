package subscribers.clearbunyang.domain.dashBoard.dto.response;


import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PropertiesInquiryStatusResponse {
    private int totalPages;
    private int pageSize;
    private int currentPage;
    private List<PropertyInquiryStatusResponse> contents;
    private long totalElements;

    public static PropertiesInquiryStatusResponse of(Page<PropertyInquiryStatusResponse> page) {
        return PropertiesInquiryStatusResponse.builder()
                .totalPages(page.getTotalPages())
                .pageSize(page.getSize())
                .currentPage(page.getNumber())
                .contents(page.getContent())
                .totalElements(page.getTotalElements())
                .build();
    }
}
