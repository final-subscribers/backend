package subscribers.clearbunyang.domain.property.model.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.domain.property.entity.Area;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AreaResponseDTO {
    private Integer squareMeter; // 면적

    private Integer price; // 가격

    private Integer discountPrice; // 할인 가격

    private Integer discountPercent; // 할인 분양가(퍼센트)

    public static AreaResponseDTO toDTO(Area area) {
        return AreaResponseDTO.builder()
                .squareMeter(area.getSquareMeter())
                .price(area.getPrice())
                .discountPercent(area.getDiscountPercent())
                .discountPrice(area.getDiscountPrice())
                .build();
    }
}
