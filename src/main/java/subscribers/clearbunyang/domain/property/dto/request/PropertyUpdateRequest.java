package subscribers.clearbunyang.domain.property.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordName;
import subscribers.clearbunyang.domain.property.entity.enums.PropertyType;
import subscribers.clearbunyang.domain.property.entity.enums.SalesType;
import subscribers.clearbunyang.global.file.dto.FileRequestDTO;
import subscribers.clearbunyang.global.validation.NumericValidation;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyUpdateRequest {
    @NotBlank
    @Size(min = 1, max = 20)
    private String name; // 매물명

    @NotNull private SalesType salesType; // 분양 형태

    @NotNull private PropertyType propertyType; // 분양 타입

    @NotBlank
    @Size(min = 1, max = 20)
    private String constructor; // 시공사

    @NotBlank
    @Size(min = 1, max = 20)
    private String companyName; // 시행사

    @NotBlank
    @Size(min = 1, max = 20)
    private String addrDo; // 도/시

    @NotBlank
    @Size(min = 1, max = 20)
    private String addrGu; // 시/군/구

    @NotBlank
    @Size(min = 1, max = 20)
    private String addrDong; // 동

    @NotBlank
    @Size(min = 1, max = 20)
    private String buildingName; // 건물 이름

    @NotBlank
    @Size(min = 1, max = 255)
    private String areaAddr; // 대지 위치

    @NotNull @Min(1)
    @Max(10000)
    private int totalNumber; // 세대수

    @NotBlank
    @Size(min = 1, max = 255)
    private String modelhouseAddr; // 모델하우스 위치

    @NotNull private LocalDate startDate; // 모집 시작일

    @NotNull private LocalDate endDate; // 모집 종료일

    @Size(max = 255)
    private String contactChannel; // 문의 채널 링크

    @Size(max = 255)
    private String homepage; // 홈페이지 링크

    @NotBlank
    @Size(min = 1, max = 12)
    @NumericValidation
    private String phoneNumber; // 전화번호

    @NotNull @Valid private List<AreaRequest> areas; // 면적

    @NotNull @Valid private FileRequestDTO propertyImage; // 파일 이미지

    @NotNull @Valid private FileRequestDTO supplyInformation; // 공급 안내표

    @Valid private FileRequestDTO marketing; // 마케팅 자료

    @Valid private List<KeywordRequest> infra;
    @Valid private List<KeywordRequest> benefit;

    @Schema(hidden = true) // Swagger에서 숨김 처리
    @AssertTrue(
            message =
                    "키워드는 세 개 이상이어야 하고, 그 중 세 개는 꼭 검색 가능한 키워드여야 하며, DISCOUNT_SALE 키워드를 선택했다면 discountPercent 또는 discountPrice가 입력되어야 합니다.")
    public boolean isKeywordsValid() {
        if (infra == null) infra = new ArrayList<>();
        if (benefit == null) benefit = new ArrayList<>();

        if (infra.size() + benefit.size() < 3) return false;

        long infraSearchableCount = infra.stream().filter(KeywordRequest::getSearchEnabled).count();
        long benefitSearchableCount =
                benefit.stream().filter(KeywordRequest::getSearchEnabled).count();
        if (infraSearchableCount + benefitSearchableCount != 3) return false;

        boolean infraDiscountSaleExists =
                infra.stream().anyMatch(keyword -> keyword.getName() == KeywordName.DISCOUNT_SALE);
        boolean benefitDiscountSaleExists =
                benefit.stream()
                        .anyMatch(keyword -> keyword.getName() == KeywordName.DISCOUNT_SALE);

        if (infraDiscountSaleExists || benefitDiscountSaleExists) {
            return areas.stream()
                    .allMatch(
                            area ->
                                    area.getDiscountPrice() != null
                                            || area.getDiscountPercent() != null);
        }
        return true;
    }
}
