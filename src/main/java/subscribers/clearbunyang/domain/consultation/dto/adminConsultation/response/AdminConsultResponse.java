package subscribers.clearbunyang.domain.consultation.dto.adminConsultation.response;


import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.domain.consultation.entity.AdminConsultation;
import subscribers.clearbunyang.domain.consultation.entity.enums.Status;
import subscribers.clearbunyang.domain.consultation.entity.enums.Tier;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminConsultResponse {

    private String name;

    private String phoneNumber;

    private LocalDate createdAt;

    private LocalDate preferredAt;

    private String memberMessage;

    private Status status;

    private Tier tier;

    private String consultantMessage;

    public static AdminConsultResponse toDto(AdminConsultation consultation) {
        return AdminConsultResponse.builder()
                .name(consultation.getMemberConsultation().getMemberName())
                .phoneNumber(consultation.getMemberConsultation().getPhoneNumber())
                .createdAt(LocalDate.from(consultation.getMemberConsultation().getCreatedAt()))
                .preferredAt(consultation.getMemberConsultation().getPreferredAt())
                .memberMessage(consultation.getMemberConsultation().getMemberMessage())
                .status(consultation.getMemberConsultation().getStatus())
                .tier(consultation.getTier())
                .consultantMessage(consultation.getConsultMessage())
                .build();
    }
}
