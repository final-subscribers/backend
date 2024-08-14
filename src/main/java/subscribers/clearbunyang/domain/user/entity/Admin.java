package subscribers.clearbunyang.domain.user.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import subscribers.clearbunyang.domain.user.entity.enums.AdminState;
import subscribers.clearbunyang.domain.user.entity.enums.UserRole;
import subscribers.clearbunyang.global.entity.BaseEntity;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Admin extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private BigInteger phoneNumber;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private BigInteger registrationNumber;

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
}
