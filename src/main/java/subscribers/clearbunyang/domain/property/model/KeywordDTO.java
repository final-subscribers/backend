package subscribers.clearbunyang.domain.property.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeywordDTO {
    @NotBlank
    @Size(min = 1, max = 255)
    private String name;

    @NotBlank
    @Size(min = 1, max = 20)
    private String type; // BENEFIT/INFRA

    @NotNull private Boolean searchEnabled;

    @NotNull private Object input;
}
