package subscribers.clearbunyang.domain.consultation.model.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.domain.consultation.annotation.ValidStatus;
import subscribers.clearbunyang.domain.consultation.annotation.ValidTier;
import subscribers.clearbunyang.domain.consultation.entity.enums.Status;
import subscribers.clearbunyang.domain.consultation.entity.enums.Tier;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsultRequest {

    @ValidStatus(enumClass = Status.class)
    private Status status;

    @ValidTier(enumClass = Tier.class)
    private Tier tier;

    private String consultantMessage;
}
