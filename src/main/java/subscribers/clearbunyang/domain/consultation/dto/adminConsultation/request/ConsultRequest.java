package subscribers.clearbunyang.domain.consultation.dto.adminConsultation.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.domain.consultation.entity.enums.Status;
import subscribers.clearbunyang.domain.consultation.entity.enums.Tier;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsultRequest {

    @NotNull private Status status;

    @NotNull private Tier tier;

    @NotBlank private String consultantMessage;
}
