package subscribers.clearbunyang.domain.property.dto.response;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.*;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordType;
import subscribers.clearbunyang.domain.property.entity.enums.PropertyType;
import subscribers.clearbunyang.domain.property.entity.enums.SalesType;
import subscribers.clearbunyang.global.exception.InvalidValueException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;
import subscribers.clearbunyang.global.file.dto.FileResponseDTO;
import subscribers.clearbunyang.global.file.entity.enums.FileType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyDetailsResponse {

    private String name;
    private String constructor;
    private String companyName;
    private SalesType salesType; // 분양 형태
    private PropertyType propertyType; // 분양 타입
    private String buildingName; // 건물 이름
    private List<AreaResponse> areas; // 면적
    private String addrDo; // 도/시
    private String addrGu; // 구
    private String addrDong; // 동
    private String areaAddr; // 대지 위치
    private int totalNumber; // 세대수
    private String modelhouseAddr; // 모델하우스 위치
    private LocalDate startDate; // 모집 시작일
    private LocalDate endDate; // 모집 종료일
    private String contactChannel; // 문의 채널 링크
    private String homepage; // 홈페이지 링크
    private String phoneNumber; // 전화번호
    private boolean likes; // like 눌렀는지 여부
    private FileResponseDTO propertyImage; // 파일 이미지
    private FileResponseDTO supplyInformation; // 공급 안내표
    private FileResponseDTO marketing; // 마케팅 자료
    private List<KeywordResponse> infra;
    private List<KeywordResponse> benefit;

    public static PropertyDetailsResponse toDTO(
            Property property,
            Map<KeywordType, List<KeywordResponse>> categorizedKeywords,
            boolean likesExisted) {

        List<AreaResponse> areaResponses =
                property.getAreas().stream().map(AreaResponse::toDTO).collect(Collectors.toList());

        List<FileResponseDTO> fileResponseDTOS =
                property.getFiles().stream()
                        .map(FileResponseDTO::toDTO)
                        .collect(Collectors.toList());

        FileResponseDTO propertyImage =
                fileResponseDTOS.stream()
                        .filter(file -> FileType.PROPERTY_IMAGE == file.getType())
                        .findFirst()
                        .orElseThrow(
                                () -> new InvalidValueException(ErrorCode.FILE_TYPE_NOT_FOUND));
        FileResponseDTO supplyInformation =
                fileResponseDTOS.stream()
                        .filter(file -> FileType.SUPPLY_INFORMATION == file.getType())
                        .findFirst()
                        .orElseThrow(
                                () -> new InvalidValueException(ErrorCode.FILE_TYPE_NOT_FOUND));
        FileResponseDTO marketing =
                fileResponseDTOS.stream()
                        .filter(file -> FileType.MARKETING == file.getType())
                        .findFirst()
                        .orElse(null);

        List<KeywordResponse> infra =
                categorizedKeywords.getOrDefault(KeywordType.INFRA, new ArrayList<>());
        List<KeywordResponse> benefit =
                categorizedKeywords.getOrDefault(KeywordType.BENEFIT, new ArrayList<>());

        return PropertyDetailsResponse.builder()
                .name(property.getName())
                .constructor(property.getConstructor())
                .companyName(property.getCompanyName())
                .salesType(property.getSalesType())
                .propertyType(property.getPropertyType())
                .addrDo(property.getAddrDo())
                .addrGu(property.getAddrGu())
                .addrDong(property.getAddrDong())
                .buildingName(property.getBuildingName())
                .areas(areaResponses)
                .areaAddr(property.getAreaAddr())
                .totalNumber(property.getTotalNumber())
                .modelhouseAddr(property.getModelHouseAddr())
                .startDate(property.getStartDate())
                .endDate(property.getEndDate())
                .contactChannel(property.getContactChannel())
                .homepage(property.getHomePage())
                .phoneNumber(property.getPhoneNumber())
                .likes(likesExisted)
                .propertyImage(propertyImage)
                .supplyInformation(supplyInformation)
                .marketing(marketing)
                .infra(infra)
                .benefit(benefit)
                .build();
    }
}
