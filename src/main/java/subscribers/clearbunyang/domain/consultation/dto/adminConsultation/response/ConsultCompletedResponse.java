package subscribers.clearbunyang.domain.consultation.dto.adminConsultation.response;


import java.time.LocalDate;
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
public class ConsultCompletedResponse {

    private String name; // 고객 이름

    private String phoneNumber; // 전화번호

    private LocalDate createdAt; // 상담 생성일, 신청일

    private LocalDate completedAt; // 상담 완료일

    private String memberMessage; // 고객 메세지

    private Tier tier; // 등급

    private String consultMessage; // 상담원 메모

    private long totalCount;

    public static ConsultCompletedResponse toDto(AdminConsultation consultation, long totalCount) {
        return ConsultCompletedResponse.builder()
                .name(consultation.getMemberConsultation().getMemberName())
                .phoneNumber(consultation.getMemberConsultation().getPhoneNumber())
                .createdAt(LocalDate.from(consultation.getMemberConsultation().getCreatedAt()))
                .completedAt(consultation.getCompletedAt())
                .memberMessage(consultation.getMemberConsultation().getMemberMessage())
                .tier(consultation.getTier())
                .consultMessage(consultation.getConsultMessage())
                .totalCount(totalCount)
                .build();
    }

    public static ConsultCompletedResponse toDto(String consultMessage) {
        return ConsultCompletedResponse.builder().consultMessage(consultMessage).build();
    }

    public static ConsultCompletedResponse toDto(AdminConsultation consultation) {
        return ConsultCompletedResponse.builder()
                .name(consultation.getMemberConsultation().getMemberName())
                .phoneNumber(consultation.getMemberConsultation().getPhoneNumber())
                .createdAt(LocalDate.from(consultation.getMemberConsultation().getCreatedAt()))
                .completedAt(consultation.getCompletedAt())
                .memberMessage(consultation.getMemberConsultation().getMemberMessage())
                .tier(consultation.getTier())
                .consultMessage(consultation.getConsultMessage())
                .build();
    }
}
