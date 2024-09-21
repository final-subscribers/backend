package subscribers.clearbunyang.domain.consultation.dto.memberConsultations;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.domain.consultation.entity.MemberConsultation;
import subscribers.clearbunyang.global.exception.InvalidValueException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;
import subscribers.clearbunyang.global.file.dto.FileResponseDTO;
import subscribers.clearbunyang.global.file.entity.enums.FileType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyConsultationsResponse {

    private String imageUrl;
    private Long id;
    private String name;
    private LocalDate consultationCreatedAt;
    private String message;
    private String memberName;
    private String phoneNumber;
    private LocalDate preferredAt;

    public static MyConsultationsResponse toDto(MemberConsultation memberConsultation) {

        List<FileResponseDTO> fileResponseDTOS =
                memberConsultation.getProperty().getFiles().stream()
                        .map(FileResponseDTO::toDTO)
                        .collect(Collectors.toList());

        String propertyImageUrl =
                fileResponseDTOS.stream()
                        .filter(file -> FileType.PROPERTY_IMAGE == file.getType())
                        .map(FileResponseDTO::getUrl) // url만 추출
                        .findFirst()
                        .orElseThrow(
                                () -> new InvalidValueException(ErrorCode.FILE_TYPE_NOT_FOUND));

        return MyConsultationsResponse.builder()
                .imageUrl(propertyImageUrl)
                .id(memberConsultation.getProperty().getId())
                .name(memberConsultation.getProperty().getName())
                .consultationCreatedAt(memberConsultation.getCreatedAt().toLocalDate())
                .message(memberConsultation.getMemberMessage())
                .memberName(memberConsultation.getMemberName())
                .phoneNumber(memberConsultation.getPhoneNumber())
                .preferredAt(memberConsultation.getPreferredAt())
                .build();
    }
}
