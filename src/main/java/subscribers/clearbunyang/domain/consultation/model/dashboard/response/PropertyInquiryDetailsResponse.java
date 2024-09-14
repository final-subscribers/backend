package subscribers.clearbunyang.domain.consultation.model.dashboard.response;


import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.domain.consultation.model.dashboard.PropertyInquiryDetailsDTO;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PropertyInquiryDetailsResponse {
    Integer pending;
    Integer completed;
    Integer all;
    Integer phone;
    Integer channel;
    Integer lms;
    List<GraphRequirementsResponse> graphRequirements;

    public static PropertyInquiryDetailsResponse of(
            PropertyInquiryDetailsDTO dto, List<GraphRequirementsResponse> graphRequirements) {
        return PropertyInquiryDetailsResponse.builder()
                .pending(dto.getPending())
                .completed(dto.getCompleted())
                .all(dto.getAll())
                .phone(dto.getPhone())
                .channel(dto.getChannel())
                .lms(dto.getLms())
                .graphRequirements(graphRequirements)
                .build();
    }
}
