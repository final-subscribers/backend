package subscribers.clearbunyang.domain.consultation.model.response;


import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.domain.file.entity.File;
import subscribers.clearbunyang.domain.file.entity.enums.FileType;
import subscribers.clearbunyang.domain.property.entity.Property;

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
        File imageFile =
                property.getFiles().stream()
                        .filter(file -> file.getType() == FileType.PROPERTY_IMAGE)
                        .findFirst()
                        .orElse(null);
        return SideBarSelectedPropertyResponse.builder()
                .id(property.getId())
                .name(property.getName())
                .image(imageFile != null ? imageFile.getLink() : null)
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
