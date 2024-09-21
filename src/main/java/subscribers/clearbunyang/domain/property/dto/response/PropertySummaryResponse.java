package subscribers.clearbunyang.domain.property.dto.response;


import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.domain.property.entity.Property;
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
public class PropertySummaryResponse {
    private Long id;
    private String imageUrl;
    private String propertyName;
    private String areaAddr;
    private PropertyType propertyType;
    private SalesType salesType;
    private int totalNumber;
    private List<String> infra;
    private List<String> benefit;
    private int price;
    private Integer discountPrice;
    private Integer discountPercent;
    private boolean like;

    public static PropertySummaryResponse toDto(
            Property property,
            List<String> infraKeywords,
            List<String> benefitKeywords,
            boolean likeExisted) {

        List<FileResponseDTO> fileResponseDTOS =
                property.getFiles().stream()
                        .map(FileResponseDTO::toDTO)
                        .collect(Collectors.toList());

        String propertyImageUrl =
                fileResponseDTOS.stream()
                        .filter(file -> FileType.PROPERTY_IMAGE == file.getType())
                        .map(FileResponseDTO::getUrl) // url만 추출
                        .findFirst()
                        .orElseThrow(
                                () -> new InvalidValueException(ErrorCode.FILE_TYPE_NOT_FOUND));

        return PropertySummaryResponse.builder()
                .id(property.getId())
                .imageUrl(propertyImageUrl)
                .propertyName(property.getName())
                .areaAddr(property.getAreaAddr())
                .propertyType(property.getPropertyType())
                .salesType(property.getSalesType())
                .totalNumber(property.getTotalNumber())
                .infra(infraKeywords)
                .benefit(benefitKeywords)
                .price(property.getPrice())
                .discountPrice(property.getDiscountPrice())
                .discountPercent(property.getDiscountPercent())
                .like(likeExisted)
                .build();
    }
}
