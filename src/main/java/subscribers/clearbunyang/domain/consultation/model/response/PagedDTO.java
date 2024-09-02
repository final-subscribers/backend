package subscribers.clearbunyang.domain.consultation.model.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagedDTO<T> {

    private int totalPages; // 전체 페이지 수

    private int pageSize; // 한 페이지에 포함된 데이터 수

    private int currentPage; // 현 페이지 번호

    private T content;
}
