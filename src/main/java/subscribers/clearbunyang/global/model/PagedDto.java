package subscribers.clearbunyang.global.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagedDto<T> {

    private int totalPages; // 전체 페이지 수

    private int pageSize; // 한 페이지에 포함된 데이터 수

    private int currentPage; // 현 페이지 번호

    private T content;

    public static <T> PagedDto toDTO(int currentPage, int size, int totalCount, T Content) {
        return PagedDto.builder()
                .currentPage(currentPage)
                .pageSize(size)
                .totalPages((totalCount + size - 1) / size)
                .content(Content)
                .build();
    }
}
