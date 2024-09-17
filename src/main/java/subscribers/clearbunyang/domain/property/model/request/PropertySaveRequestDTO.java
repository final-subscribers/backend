package subscribers.clearbunyang.domain.property.model.request;


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
public class PropertySaveRequestDTO {
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

    @NotNull @Valid private List<AreaRequestDTO> areas; // 면적 정보 리스트

    @NotNull @Valid private List<FileRequestDTO> files; // 파일 정보 리스트

    @NotNull @Valid private List<KeywordRequestDTO> keywords; // 키워드 정보 리스트

    @AssertTrue(message = "키워드는 세개 이상이어야함")
    public boolean isKeywordsValid() {
        if (keywords.size() < 3) return false;
        return true;
    }
}
