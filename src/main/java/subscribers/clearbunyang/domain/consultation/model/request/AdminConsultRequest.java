package subscribers.clearbunyang.domain.consultation.model.request;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.domain.consultation.annotation.ValidStatus;
import subscribers.clearbunyang.domain.consultation.entity.enums.Status;
import subscribers.clearbunyang.domain.consultation.entity.enums.Tier;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminConsultRequest {

    @ValidStatus(enumClass = Status.class)
    private Status status;

    @ValidStatus(enumClass = Tier.class)
    private Tier tier;

    @NotNull private String consultantMessage;
}
