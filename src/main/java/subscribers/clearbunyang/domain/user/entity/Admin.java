package subscribers.clearbunyang.domain.user.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import subscribers.clearbunyang.domain.file.entity.File;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.user.entity.enums.AdminState;
import subscribers.clearbunyang.domain.user.entity.enums.UserRole;
import subscribers.clearbunyang.global.entity.BaseEntity;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table
public class Admin extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Long phoneNumber;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private Long registrationNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String business;

    @Setter
    @Enumerated(EnumType.STRING)
    private AdminState status;

    @Setter
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.ADMIN;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private List<Property> properties = new ArrayList<>();

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();
}
