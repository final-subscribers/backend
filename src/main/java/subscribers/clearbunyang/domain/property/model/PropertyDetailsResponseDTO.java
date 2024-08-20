package subscribers.clearbunyang.domain.property.model;


import java.util.List;
import lombok.*;
import subscribers.clearbunyang.domain.property.entity.enums.SalesType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyDetailsResponseDTO {

    private String imageUrl; // 매물 사진
    private SalesType salesType; // 분양 형태
    private List<AreaDTO> area; // 면적
    private String areaAddr; // 대지 위치
    private int totalNumber; // 세대수
    private String modelhouseAddr; // 모델하우스 위치
    private String startDate; // 모집 시작일
    private String endDate; // 모집 종료일
    private String contactChannel; // 문의 채널 링크
    private String homepage; // 홈페이지 링크
    private String phoneNumber; // 전화번호
    private boolean likes; // like 눌렀는지 여부
    private FileDTO supplyInformationFile; // 공급 안내표 파일
    private FileDTO marketingFile; // 마케팅 파일
    private List<KeywordDTO> keywords; // 혜택 줍줍/주변핵심체크
}
