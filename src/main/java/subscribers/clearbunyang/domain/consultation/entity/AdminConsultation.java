package subscribers.clearbunyang.domain.consultation.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import subscribers.clearbunyang.domain.consultation.dto.adminConsultation.request.ConsultRequest;
import subscribers.clearbunyang.domain.consultation.entity.enums.Tier;
import subscribers.clearbunyang.global.entity.BaseEntity;

@Entity
@Table(name = "admin_consultation")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AdminConsultation extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private Tier tier;

    private String consultMessage;

    private String consultant;

    private LocalDate completedAt;

    @OneToOne(mappedBy = "adminConsultation", cascade = CascadeType.ALL)
    @JsonManagedReference
    private MemberConsultation memberConsultation;

    public void setConsultant(String consultant) {
        this.consultant = consultant;
    }

    public void setTier(Tier tier) {
        this.tier = tier;
    }

    public void setMessage(String consultMessage) {
        this.consultMessage = consultMessage;
    }

    public void update(ConsultRequest request, MemberConsultation consultation) {
        this.tier = request.getTier();
        this.consultMessage = request.getConsultantMessage();
        this.consultant = consultation.getAdminConsultation().getConsultant();
        this.completedAt = LocalDate.now();
        this.memberConsultation = consultation;
    }
}
