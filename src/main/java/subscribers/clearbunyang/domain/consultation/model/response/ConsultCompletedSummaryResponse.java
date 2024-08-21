package subscribers.clearbunyang.domain.consultation.model.response;


import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.domain.consultation.entity.AdminConsultation;
import subscribers.clearbunyang.domain.consultation.entity.enums.Tier;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultCompletedSummaryResponse {

    private String name;

    private String consultant;

    private LocalDateTime createdAt;

    private LocalDate completedAt;

    private Tier tier;

    private String phoneNumber;

    public static ConsultCompletedSummaryResponse toDto(AdminConsultation consultation) {
        return ConsultCompletedSummaryResponse.builder()
                .name(consultation.getMemberConsultation().getMemberName())
                .consultant(consultation.getConsultant())
                .createdAt(consultation.getMemberConsultation().getCreatedAt())
                .completedAt(consultation.getCompletedAt())
                .tier(consultation.getTier())
                .phoneNumber(consultation.getMemberConsultation().getPhoneNumber())
                .build();
    }
}
