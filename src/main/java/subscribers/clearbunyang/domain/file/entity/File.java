package subscribers.clearbunyang.domain.file.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import subscribers.clearbunyang.domain.file.entity.enums.FileType;
import subscribers.clearbunyang.domain.file.model.FileDTO;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.user.entity.Admin;
import subscribers.clearbunyang.global.entity.BaseEntity;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "file")
public class File extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    private Property property;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String link;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FileType type;

    public static File toEntity(FileDTO fileDTO, Property property, Admin admin) {
        return File.builder()
                .property(property)
                .admin(admin)
                .name(fileDTO.getName())
                .link(fileDTO.getUrl())
                .type(fileDTO.getType())
                .build();
    }
}
