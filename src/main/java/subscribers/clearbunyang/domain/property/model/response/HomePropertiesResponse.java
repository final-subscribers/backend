package subscribers.clearbunyang.domain.property.model.response;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.domain.file.model.FileResponseDTO;
import subscribers.clearbunyang.domain.property.entity.Keyword;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.entity.enums.SalesType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomePropertiesResponse {
    private Long id;
    private FileResponseDTO imageUrl;
    private String propertyName;
    private String areaAddr;
    private SalesType salesType;
    private int totalNumber;
    private List<Keyword> keywords;
    private int price;
    private int discountPrice;
    private boolean like;

    public static HomePropertiesResponse toDto(Property property) {
        return HomePropertiesResponse.builder()
                .id(property.getId())
                //            .imageUrl 이미지 가져오는 처리 필요
                .propertyName(property.getName())
                .areaAddr(property.getAreaAddr())
                .salesType(property.getSalesType())
                .totalNumber(property.getTotalNumber())
                .keywords(property.getKeywords())
                .price(property.getAreas().get(0).getPrice())
                .discountPrice(property.getAreas().get(0).getDiscountPrice())
                .build();
    }
}
