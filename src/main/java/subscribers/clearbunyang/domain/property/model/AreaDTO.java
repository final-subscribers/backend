package subscribers.clearbunyang.domain.property.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AreaDTO {
    private int squareMeter; // 면적
    private int price; // 가격
    private int discountPrice; // 할인 분양가
}
