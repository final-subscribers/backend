package subscribers.clearbunyang.domain.consultation.model.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.domain.consultation.annotation.ValidMedium;
import subscribers.clearbunyang.domain.consultation.annotation.ValidStatus;
import subscribers.clearbunyang.domain.consultation.annotation.ValidTier;
import subscribers.clearbunyang.domain.consultation.entity.enums.Medium;
import subscribers.clearbunyang.domain.consultation.entity.enums.Status;
import subscribers.clearbunyang.domain.consultation.entity.enums.Tier;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewCustomerAdditionRequest {

    @NotBlank private String name; // 고객 이름

    @NotBlank private String phoneNumber; // 고객 폰 번호

    @NotBlank private String consultant; // 상담사

    @NotNull private LocalDate preferredAt;

    private String consultingMessage; // 상담원 작성 메세지

    @ValidTier(enumClass = Tier.class) // pending 일 때 입력하지 않으면 에러
    private Tier tier; // 등급

    @ValidStatus(enumClass = Status.class)
    private Status status; // 상태

    @ValidMedium(enumClass = Medium.class)
    private Medium medium;
}
