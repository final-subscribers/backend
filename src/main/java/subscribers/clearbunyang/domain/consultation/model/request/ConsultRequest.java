package subscribers.clearbunyang.domain.consultation.model.request;


import jakarta.validation.constraints.NotBlank;
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

    private Status status;

    private Tier tier;

    @NotBlank private String consultantMessage;
}
