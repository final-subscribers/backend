package subscribers.clearbunyang.domain.property.model;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AreaDTO {
    @NotNull @Min(1)
    @Max(300)
    private Integer squareMeter; // 면적

    @NotNull @Min(1)
    @Max(5000000)
    private Integer price; // 가격

    @NotNull @Min(1)
    @Max(100)
    private Integer discountPercent; // 할인 분양가(퍼센트)
}
