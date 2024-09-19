package subscribers.clearbunyang.domain.consultation.dto.memberConsultations;


import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.domain.consultation.entity.MemberConsultation;
import subscribers.clearbunyang.global.file.dto.FileResponseDTO;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyConsultationsResponse {

    private FileResponseDTO imageUrl;
    private Long id;
    private String name;
    private LocalDate consultationCreatedAt;
    private String message;
    private String memberName;
    private String phoneNumber;
    private LocalDate preferredAt;

    public static MyConsultationsResponse toDto(MemberConsultation memberConsultation) {
        return MyConsultationsResponse.builder()
                //            .imageUrl()  이미지 가져오는 처리 필요
                .id(memberConsultation.getProperty().getId())
                .name(memberConsultation.getMemberName())
                .consultationCreatedAt(memberConsultation.getCreatedAt().toLocalDate())
                .message(memberConsultation.getMemberMessage())
                .memberName(memberConsultation.getMemberName())
                .phoneNumber(memberConsultation.getPhoneNumber())
                .preferredAt(memberConsultation.getPreferredAt())
                .build();
    }
}
