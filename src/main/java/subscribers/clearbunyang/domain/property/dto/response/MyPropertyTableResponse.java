package subscribers.clearbunyang.domain.property.dto.response;


import java.time.LocalDate;
import lombok.*;
import subscribers.clearbunyang.domain.property.entity.Property;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPropertyTableResponse {
    private Long id; // 매물 id
    private boolean isPending; // pending상태인지
    private String name; // 매물명
    private int totalCount; // 세대수
    private Long consultationPendingCount; // 상담 대기 개수
    private LocalDate createdAt; // 생성 날짜
    private LocalDate endDate; // 모집 종료일

    public static MyPropertyTableResponse toDTO(Property property, Long consultationPendingCount) {
        return MyPropertyTableResponse.builder()
                .id(property.getId())
                .isPending(!LocalDate.now().isAfter(property.getEndDate()))
                .name(property.getName())
                .totalCount(property.getTotalNumber())
                .consultationPendingCount(consultationPendingCount)
                .createdAt(property.getCreatedAt().toLocalDate())
                .endDate(property.getEndDate())
                .build();
    }
}
