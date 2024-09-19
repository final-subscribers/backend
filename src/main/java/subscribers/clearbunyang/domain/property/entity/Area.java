package subscribers.clearbunyang.domain.property.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import subscribers.clearbunyang.domain.property.dto.request.AreaRequest;
import subscribers.clearbunyang.global.entity.BaseEntity;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "area")
public class Area extends BaseEntity {

    private int squareMeter;

    private int price;

    private Integer discountPercent;

    private Integer discountPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    public static Area toEntity(AreaRequest areaRequest, Property property) {
        return Area.builder()
                .squareMeter(areaRequest.getSquareMeter())
                .price(areaRequest.getPrice())
                .discountPrice(areaRequest.getDiscountPrice())
                .discountPercent(areaRequest.getDiscountPercent())
                .property(property)
                .build();
    }
}
