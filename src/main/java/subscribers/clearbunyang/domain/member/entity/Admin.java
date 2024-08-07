package subscribers.clearbunyang.domain.member.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import subscribers.clearbunyang.domain.member.entity.enums.AdminState;
import subscribers.clearbunyang.domain.member.entity.enums.MemberRole;
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
    private Integer companyNumber;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private Integer registrationNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String business;

    @Setter
    @Enumerated(EnumType.STRING)
    private AdminState adminState;

    @Setter
    @Enumerated(EnumType.STRING)
    private MemberRole memberRole = MemberRole.ADMIN;
}
