package subscribers.clearbunyang.domain.property.model.response;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.*;
import subscribers.clearbunyang.domain.file.entity.enums.FileType;
import subscribers.clearbunyang.domain.file.model.FileResponseDTO;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordType;
import subscribers.clearbunyang.domain.property.entity.enums.PropertyType;
import subscribers.clearbunyang.domain.property.entity.enums.SalesType;
import subscribers.clearbunyang.global.exception.Invalid.InvalidValueException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyDetailsResponseDTO {

    private SalesType salesType; // 분양 형태
    private PropertyType propertyType; // 분양 타입
    private String buildingName; // 건물 이름
    private List<AreaResponseDTO> areas; // 면적
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
    private List<KeywordResponseDTO> infra;
    private List<KeywordResponseDTO> benefit;

    public static PropertyDetailsResponseDTO toDTO(
            Property property,
            Map<KeywordType, List<KeywordResponseDTO>> categorizedKeywords,
            boolean likesExisted) {

        List<AreaResponseDTO> areaResponseDTOS =
                property.getAreas().stream()
                        .map(AreaResponseDTO::toDTO)
                        .collect(Collectors.toList());

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
                        .orElseThrow(
                                () -> new InvalidValueException(ErrorCode.FILE_TYPE_NOT_FOUND));

        List<KeywordResponseDTO> infra =
                categorizedKeywords.getOrDefault(KeywordType.INFRA, new ArrayList<>());
        List<KeywordResponseDTO> benefit =
                categorizedKeywords.getOrDefault(KeywordType.BENEFIT, new ArrayList<>());

        return PropertyDetailsResponseDTO.builder()
                .salesType(property.getSalesType())
                .propertyType(property.getPropertyType())
                .buildingName(property.getBuildingName())
                .areas(areaResponseDTOS)
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
