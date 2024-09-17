package subscribers.clearbunyang.domain.property.model.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.domain.file.model.FileRequestDTO;
import subscribers.clearbunyang.domain.property.entity.enums.PropertyType;
import subscribers.clearbunyang.domain.property.entity.enums.SalesType;
import subscribers.clearbunyang.global.validation.NumericValidation;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyUpdateRequestDTO {
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

    @NotNull @Valid private List<AreaRequestDTO> areas; // 면적

    @NotNull @Valid private FileRequestDTO propertyImage; // 파일 이미지

    @NotNull @Valid private FileRequestDTO supplyInformation; // 공급 안내표

    @Valid private FileRequestDTO marketing; // 마케팅 자료

    @Valid private List<KeywordRequestDTO> infra;
    @Valid private List<KeywordRequestDTO> benefit;

    @Schema(hidden = true) // Swagger에서 숨김 처리
    @AssertTrue(message = "infra와 benefit 둘다 모두 null 이면 안됨")
    public boolean isKeywordsValid() {
        if (infra == null && benefit == null) return false;
        if (infra.size() + benefit.size() < 3) return false;
        return true;
    }
}
