package subscribers.clearbunyang.global.file.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import subscribers.clearbunyang.domain.auth.entity.Admin;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.global.entity.BaseEntity;
import subscribers.clearbunyang.global.file.dto.FileRequestDTO;
import subscribers.clearbunyang.global.file.entity.enums.FileType;

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
