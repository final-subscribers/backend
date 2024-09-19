package subscribers.clearbunyang.domain.property.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.domain.property.entity.Area;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AreaResponse {
    private Integer squareMeter; // 면적

    private Integer price; // 가격

    private Integer discountPrice; // 할인 가격

    private Integer discountPercent; // 할인 분양가(퍼센트)

    public static AreaResponse toDTO(Area area) {
        return AreaResponse.builder()
                .squareMeter(area.getSquareMeter())
                .price(area.getPrice())
                .discountPercent(area.getDiscountPercent())
                .discountPrice(area.getDiscountPrice())
                .build();
    }
}
