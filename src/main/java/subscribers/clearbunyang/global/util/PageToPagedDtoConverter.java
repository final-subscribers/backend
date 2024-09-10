package subscribers.clearbunyang.global.util;


import org.springframework.data.domain.Page;
import subscribers.clearbunyang.global.model.PagedDto;

public class PageToPagedDtoConverter {
    public static <T> PagedDto<T> convertToPagedDto(Page<T> page) {
        return PagedDto.<T>builder()
                .totalPages(page.getTotalPages())
                .pageSize(page.getSize())
                .currentPage(page.getNumber())
                .content((T) page.getContent())
                .build();
    }
}
