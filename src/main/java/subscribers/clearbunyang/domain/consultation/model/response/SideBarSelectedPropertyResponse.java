package subscribers.clearbunyang.domain.consultation.model.response;


import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.domain.property.entity.Property;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SideBarSelectedPropertyResponse {

    private Long id;

    private String name;

    // private FileType image;

    private String propertyName; // 분양유형

    private String companyName; // 시행사

    private String constructor; // 시공사

    private int totalNumber; // 세대수

    private LocalDate startDate; // 모집 기간

    private LocalDate endDate;

    private String propertyType;

    public static SideBarSelectedPropertyResponse toDto(Property property) {
        return SideBarSelectedPropertyResponse.builder()
                .id(property.getId())
                .name(property.getName())
                // .image(property.getFiles().get(0).getType()) TODO 파일 이미지 생성
                .propertyName(property.getName())
                .companyName(property.getCompanyName())
                .constructor(property.getConstructor())
                .totalNumber(property.getTotalNumber())
                .startDate(property.getStartDate())
                .endDate(property.getEndDate())
                .propertyType(property.getPropertyType().name())
                .build();
    }
}
