package subscribers.clearbunyang.domain.file.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import subscribers.clearbunyang.domain.file.entity.enums.FileType;
import subscribers.clearbunyang.domain.file.model.FileRequestDTO;
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
    @JoinColumn(name = "admin_id", nullable = true)
    private Admin admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = true)
    private Property property;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String link;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FileType type;

    public static File toEntity(FileRequestDTO fileRequestDTO, Property property) {
        return File.builder()
                .property(property)
                .admin(null)
                .name(fileRequestDTO.getName())
                .link(fileRequestDTO.getUrl())
                .type(fileRequestDTO.getType())
                .build();
    }
}
