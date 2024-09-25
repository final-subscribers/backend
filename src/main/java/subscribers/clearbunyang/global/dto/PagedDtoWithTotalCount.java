package subscribers.clearbunyang.global.dto;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PagedDtoWithTotalCount<T> extends PagedDto<T> {

    private Long totalCount; // 총 데이터 수

    @Builder(builderMethodName = "extendedBuilder")
    public PagedDtoWithTotalCount(
            int totalPages, int pageSize, int currentPage, List<T> contents, Long totalCount) {
        super(totalPages, pageSize, currentPage, contents); // 부모 클래스 생성자 호출
        this.totalCount = totalCount;
    }

    public static <T> PagedDtoWithTotalCount<T> toDTO(
            int currentPage, int size, int totalPages, Long totalCount, List<T> contents) {
        return PagedDtoWithTotalCount.<T>extendedBuilder()
                .currentPage(currentPage)
                .pageSize(size)
                .totalPages(totalPages)
                .contents(contents)
                .totalCount(totalCount)
                .build();
    }

    public static <T> PagedDtoWithTotalCount<T> toDTO(Page<T> page, Long totalCount) {
        return PagedDtoWithTotalCount.<T>extendedBuilder()
                .totalPages(page.getTotalPages())
                .pageSize(page.getSize())
                .currentPage(page.getNumber())
                .contents(page.getContent())
                .totalCount(totalCount)
                .build();
    }
}
