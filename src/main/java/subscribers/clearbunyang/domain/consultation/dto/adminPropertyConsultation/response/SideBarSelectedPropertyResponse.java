package subscribers.clearbunyang.domain.consultation.dto.adminPropertyConsultation.response;


import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.global.file.entity.File;
import subscribers.clearbunyang.global.file.entity.enums.FileType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SideBarSelectedPropertyResponse {

    private Long id;

    private String name;

    private String image;

    private String propertyName; // 분양유형

    private String companyName; // 시행사

    private String constructor; // 시공사

    private int totalNumber; // 세대수

    private LocalDate startDate; // 모집 기간

    private LocalDate endDate;

    private String propertyType;

    public static SideBarSelectedPropertyResponse toDto(Property property) {
        String url =
                property.getFiles().stream()
                        .filter(file -> FileType.PROPERTY_IMAGE == file.getType())
                        .findFirst()
                        .map(File::getLink)
                        .orElse(null);
        return SideBarSelectedPropertyResponse.builder()
                .id(property.getId())
                .name(property.getName())
                .image(url)
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
