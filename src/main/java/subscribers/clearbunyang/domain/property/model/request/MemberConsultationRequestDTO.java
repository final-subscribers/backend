package subscribers.clearbunyang.domain.property.model.request;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.global.validation.NumericValidation;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberConsultationRequestDTO {
    @NotBlank
    @Size(min = 1, max = 20)
    private String name;

    @NotBlank
    @Size(min = 1, max = 12)
    @NumericValidation
    private String phoneNumber;

    @NotNull @FutureOrPresent private LocalDate preferredAt;

    @Size(max = 255)
    private String counselingMessage;
}
