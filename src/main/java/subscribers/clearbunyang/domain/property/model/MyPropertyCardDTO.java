package subscribers.clearbunyang.domain.property.model;


import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPropertyCardDTO implements MyPropertyDTO {
    private Long id; // 매물 id
    private String imageUrl; // 대표 사진 url
    private String name; // 매물명
    private String address; // 주소
    private String createdAt; // 생성 날짜
    private String endDate; // 모집 종료일
}
