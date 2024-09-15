package subscribers.clearbunyang.domain.consultation.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import subscribers.clearbunyang.domain.consultation.entity.enums.Medium;
import subscribers.clearbunyang.domain.consultation.entity.enums.Status;
import subscribers.clearbunyang.domain.consultation.model.request.NewCustomerAdditionRequest;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.model.request.ConsultationRequestDTO;
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
    private LocalDate preferredAt;

    private String memberName;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Medium medium;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonBackReference
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    @JsonBackReference
    private Property property;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "admin_consultation_id")
    @JsonBackReference
    private AdminConsultation adminConsultation;

    public static MemberConsultation toEntity(
            NewCustomerAdditionRequest request, Property property, AdminConsultation consultation) {
        return MemberConsultation.builder()
                .status(request.getStatus())
                .memberMessage(null)
                .preferredAt(request.getPreferredAt())
                .memberName(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .medium(request.getMedium())
                .member(null)
                .property(property)
                .adminConsultation(consultation)
                .build();
    }

    public static MemberConsultation toEntity(
            ConsultationRequestDTO requestDTO,
            Property property,
            Member member,
            AdminConsultation adminConsultation) {
        return MemberConsultation.builder()
                .memberName(requestDTO.getName())
                .phoneNumber(requestDTO.getPhoneNumber())
                .preferredAt(requestDTO.getPreferredAt())
                .memberMessage(requestDTO.getCounselingMessage())
                .medium(Medium.LMS)
                .status(Status.PENDING)
                .property(property)
                .member(member)
                .adminConsultation(adminConsultation)
                .build();
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
