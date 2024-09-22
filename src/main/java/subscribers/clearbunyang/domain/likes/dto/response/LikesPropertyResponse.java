package subscribers.clearbunyang.domain.likes.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.domain.property.entity.Area;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.global.file.entity.File;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikesPropertyResponse {

    private Long id;

    private List<String> infra;

    private List<String> benefit;

    @JsonProperty("imageUrl")
    private String imageUrl;

    private String name;

    @JsonProperty("areaAddr")
    private String areaAddr;

    @JsonProperty("propertyType")
    private String propertyType;

    @JsonProperty("salesType")
    private String salesType;

    private int count;

    private int price;

    @JsonProperty("salesPrice")
    private int salesPrice;

    private Integer discountPercent;

    public static LikesPropertyResponse fromEntity(
            Property property, List<String> infraKeywords, List<String> benefitKeywords) {

        String imageUrl = property.getFiles().stream().findFirst().map(File::getLink).orElse(null);

        Area selectedArea = property.getAreas().stream().findFirst().orElse(null);

        int price = (selectedArea != null) ? selectedArea.getPrice() : 0;
        int discountPrice =
                (selectedArea != null && selectedArea.getDiscountPrice() != null)
                        ? selectedArea.getDiscountPrice()
                        : 0;

        return LikesPropertyResponse.builder()
                .id(property.getId())
                .infra(infraKeywords)
                .benefit(benefitKeywords)
                .imageUrl(imageUrl)
                .name(property.getName())
                .areaAddr(property.getAddrDo() + " " + property.getAddrGu())
                .propertyType(property.getPropertyType().name())
                .salesType(property.getSalesType().name())
                .count(property.getTotalNumber())
                .price(price)
                .salesPrice(discountPrice)
                .discountPercent(property.getDiscountPercent())
                .build();
    }
}
