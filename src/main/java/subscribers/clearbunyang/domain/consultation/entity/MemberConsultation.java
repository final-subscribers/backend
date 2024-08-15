package subscribers.clearbunyang.domain.consultation.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import subscribers.clearbunyang.domain.consultation.entity.enums.Medium;
import subscribers.clearbunyang.domain.consultation.entity.enums.Status;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.user.entity.Member;
import subscribers.clearbunyang.global.entity.BaseEntity;

@Entity
@Table(name = "member_consultation")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class MemberConsultation extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private String memberMessage;

    @Column(nullable = false)
    private LocalDateTime preferredAt;

    private String memberName;

    private Long phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Medium medium;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    private Property property;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_consultation_id")
    private AdminConsultation adminConsultation;
}
