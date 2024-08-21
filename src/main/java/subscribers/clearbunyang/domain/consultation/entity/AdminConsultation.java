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
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import subscribers.clearbunyang.domain.consultation.entity.enums.Tier;
import subscribers.clearbunyang.global.entity.BaseEntity;

@Entity
@Table(name = "admin_consultation")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AdminConsultation extends BaseEntity {

    @Setter
    @Enumerated(EnumType.STRING)
    private Tier tier;

    private String consultMessage;

    @Setter private String consultant;

    private LocalDate completedAt;

    @OneToOne(mappedBy = "adminConsultation", cascade = CascadeType.ALL)
    @JsonManagedReference
    private MemberConsultation memberConsultation;
}
