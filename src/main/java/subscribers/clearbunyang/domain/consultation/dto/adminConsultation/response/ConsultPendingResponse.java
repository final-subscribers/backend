package subscribers.clearbunyang.domain.consultation.dto.adminConsultation.response;


import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.domain.consultation.entity.MemberConsultation;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultPendingResponse {

    private String memberName; // 고객 name

    private String propertyName; // 매물 이름

    private String phoneNumber;

    private LocalDate createdAt;

    private LocalDate preferredAt;

    private String memberMessage;

    private String consultantMessage;

    private Boolean addConsultation;

    private long totalCount;

    public static ConsultPendingResponse toDto(
            MemberConsultation consultation, Boolean extra, long totalCount) {
        return ConsultPendingResponse.builder()
                .memberName(consultation.getMemberName())
                .propertyName(consultation.getProperty().getName())
                .phoneNumber(consultation.getPhoneNumber())
                .createdAt(LocalDate.from(consultation.getCreatedAt()))
                .preferredAt(consultation.getPreferredAt())
                .memberMessage(consultation.getMemberMessage())
                .consultantMessage(consultation.getAdminConsultation().getConsultMessage())
                .addConsultation(extra)
                .totalCount(totalCount)
                .build();
    }
}
