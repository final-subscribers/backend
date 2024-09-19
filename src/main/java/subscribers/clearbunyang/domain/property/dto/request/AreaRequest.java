package subscribers.clearbunyang.domain.property.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
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
public class AreaRequest {
    @NotNull @Min(1)
    @Max(300)
    private Integer squareMeter; // 면적

    @NotNull @Min(1)
    @Max(5000000)
    private Integer price; // 가격

    @Min(1)
    @Max(5000000)
    private Integer discountPrice; // 할인 가격

    @Min(1)
    @Max(100)
    private Integer discountPercent; // 할인 분양가(퍼센트)

    @Schema(hidden = true) // Swagger에서 숨김 처리
    @AssertTrue(message = "price>=discountPrice")
    public boolean isPriceValid() {
        if (discountPrice != null) return price >= discountPrice;
        return true;
    }
}
