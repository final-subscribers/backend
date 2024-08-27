package subscribers.clearbunyang.domain.property.model;


import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPropertyTableDTO implements MyPropertyDTO {
    private Long id; // 매물 id
    private String name; // 매물명
    private String createdAt; // 생성 날짜
    private String endDate; // 모집 종료일

    private int totalNumber; // 세대수
    private int pending; // 상담 대기수
}
