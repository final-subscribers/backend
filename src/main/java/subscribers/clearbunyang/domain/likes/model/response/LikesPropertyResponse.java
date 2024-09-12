package subscribers.clearbunyang.domain.likes.model.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.domain.file.entity.File;
import subscribers.clearbunyang.domain.property.entity.Area;
import subscribers.clearbunyang.domain.property.entity.Property;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikesPropertyResponse {

    private Long id;

    private List<String[]> keyword;

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

    public static LikesPropertyResponse fromEntity(Property property) {

        String imageUrl = property.getFiles().stream().findFirst().map(File::getLink).orElse(null);

        Area selectedArea = property.getAreas().stream().findFirst().orElse(null);

        int price = (selectedArea != null) ? selectedArea.getPrice() : 0;
        int discountPrice = (selectedArea != null) ? selectedArea.getDiscountPrice() : 0;

        return LikesPropertyResponse.builder()
                .id(property.getId())
                .keyword(
                        property.getKeywords().stream()
                                .limit(3)
                                .map(
                                        k ->
                                                new String[] {
                                                    String.valueOf(k.getName()),
                                                    String.valueOf(k.getType())
                                                })
                                .collect(Collectors.toList()))
                .imageUrl(imageUrl)
                .name(property.getName())
                .areaAddr(property.getAddrDo() + " " + property.getAddrGu())
                .propertyType(property.getPropertyType().name())
                .salesType(property.getSalesType().name())
                .count(property.getTotalNumber())
                .price(price)
                .salesPrice(discountPrice)
                .build();
    }
}
