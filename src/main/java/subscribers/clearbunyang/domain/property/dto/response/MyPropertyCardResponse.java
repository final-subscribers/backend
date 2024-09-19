package subscribers.clearbunyang.domain.property.dto.response;


import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;
import subscribers.clearbunyang.domain.property.entity.Property;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPropertyCardResponse {
    private Long id; // 매물 id
    private String imageUrl; // 대표 사진 url
    private boolean isPending; // pending상태인지
    private String name; // 매물명
    private String addrDo; // 주소-도/시
    private String addrGu; // 주소-구
    private LocalDateTime createdAt; // 생성 날짜
    private LocalDate endDate; // 모집 종료일

    public static MyPropertyCardResponse toDTO(Property property) {
        return MyPropertyCardResponse.builder()
                .id(property.getId())
                .imageUrl(property.getImageUrl())
                .isPending(!LocalDate.now().isAfter(property.getEndDate()))
                .name(property.getName())
                .addrDo(property.getAddrDo())
                .addrGu(property.getAddrGu())
                .createdAt(property.getCreatedAt())
                .endDate(property.getEndDate())
                .build();
    }
}
