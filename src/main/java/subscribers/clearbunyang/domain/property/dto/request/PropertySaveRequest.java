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
public class PropertySaveRequest {
    @NotBlank
    @Size(min = 1, max = 20)
    private String name; // 매물명

    @NotBlank
    @Size(min = 1, max = 20)
    private String constructor; // 시공사

    @NotBlank
    @Size(min = 1, max = 20)
    private String companyName; // 시행사

    @NotNull @Min(1)
    @Max(10000)
    private Integer totalNumber; // 세대 수

    @NotNull private LocalDate startDate; // 시작 기간

    @NotNull private LocalDate endDate; // 마감 기한

    @NotNull private PropertyType propertyType; // 분양 유형

    @NotNull private SalesType salesType; // 분양 형태

    @NotBlank
    @Size(min = 1, max = 255)
    private String areaAddr; // 대지 주소

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
    private String modelhouseAddr; // 모델하우스 주소

    @NotBlank
    @Size(min = 1, max = 12)
    @NumericValidation
    private String phoneNumber; // 분양 문의 번호

    @Size(max = 255)
    private String homepage; // 홈페이지 링크

    @Size(max = 255)
    private String contactChannel; // 채널 링크

    @NotNull @Valid private List<AreaRequest> areas; // 면적 정보 리스트

    @NotNull @Valid private List<FileRequestDTO> files; // 파일 정보 리스트

    @NotNull @Valid private List<KeywordRequest> keywords; // 키워드 정보 리스트

    @Schema(hidden = true) // Swagger에서 숨김 처리
    @AssertTrue(
            message =
                    "키워드는 세 개 이상이어야 하고, 그 중 세 개는 꼭 검색 가능한 키워드여야 하며, DISCOUNT_SALE 키워드를 선택했다면 discountPercent 또는 discountPrice가 입력되어야 합니다.")
    public boolean isKeywordsValid() {
        if (keywords == null) keywords = new ArrayList<>();

        if (keywords.size() < 3) return false;
        long searchableCount = keywords.stream().filter(KeywordRequest::getSearchEnabled).count();
        if (searchableCount != 3) return false;

        boolean discountSaleExists =
                keywords.stream()
                        .anyMatch(keyword -> keyword.getName() == KeywordName.DISCOUNT_SALE);
        if (discountSaleExists) {
            return areas.stream()
                    .allMatch(
                            area ->
                                    area.getDiscountPrice() != null
                                            || area.getDiscountPercent() != null);
        } else {
            return true;
        }
    }
}
