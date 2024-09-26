package subscribers.clearbunyang.domain.consultation.dto.adminPropertyConsultation.response;


import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import subscribers.clearbunyang.domain.consultation.entity.MemberConsultation;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class ConsultPendingSummaryResponse {

    private Long memberConsultationId;

    private LocalDate preferredAt;

    private LocalDate createdAt;

    private String consultant;

    private String name;

    private String phoneNumber;

    private Boolean addConsultation;

    public static ConsultPendingSummaryResponse toDto(
            MemberConsultation memberConsultations, Boolean addConsultation) {
        return ConsultPendingSummaryResponse.builder()
                .memberConsultationId(memberConsultations.getId())
                .preferredAt(memberConsultations.getPreferredAt())
                .createdAt(LocalDate.from(memberConsultations.getCreatedAt()))
                .consultant(memberConsultations.getAdminConsultation().getConsultant())
                .name(memberConsultations.getMemberName())
                .phoneNumber(memberConsultations.getPhoneNumber())
                .addConsultation(addConsultation)
                .build();
    }
}
