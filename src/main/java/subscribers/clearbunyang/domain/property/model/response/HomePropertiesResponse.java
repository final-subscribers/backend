package subscribers.clearbunyang.domain.property.model.response;


import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.domain.file.entity.enums.FileType;
import subscribers.clearbunyang.domain.file.model.FileResponseDTO;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.entity.enums.SalesType;
import subscribers.clearbunyang.global.exception.Invalid.InvalidValueException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomePropertiesResponse {
    private Long id;
    private String imageUrl;
    private String propertyName;
    private String areaAddr;
    private SalesType salesType;
    private int totalNumber;
    private List<String> infra;
    private List<String> benefit;
    private int price;
    private Integer discountPrice;
    private boolean like;

    public static HomePropertiesResponse toDto(
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

        return HomePropertiesResponse.builder()
                .id(property.getId())
                .imageUrl(propertyImageUrl)
                .propertyName(property.getName())
                .areaAddr(property.getAreaAddr())
                .salesType(property.getSalesType())
                .totalNumber(property.getTotalNumber())
                .infra(infraKeywords)
                .benefit(benefitKeywords)
                .price(property.getPrice())
                .discountPrice(property.getDiscountPrice())
                .like(likeExisted)
                .build();
    }
}
