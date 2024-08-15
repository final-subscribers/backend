package subscribers.clearbunyang.domain.consultation.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import subscribers.clearbunyang.domain.consultation.entity.enums.Rankk;
import subscribers.clearbunyang.global.entity.BaseEntity;

@Entity
@Table(name = "admin_consultation")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AdminConsultation extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private Rankk rankk;

    private String consultMessage;

    private String consultant;

    private int seatNumber;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime completedAt;

    @OneToOne(mappedBy = "adminConsultation", cascade = CascadeType.ALL)
    private MemberConsultation memberConsultation;
}
