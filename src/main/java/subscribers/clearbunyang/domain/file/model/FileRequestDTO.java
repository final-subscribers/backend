package subscribers.clearbunyang.domain.file.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.domain.file.entity.enums.FileType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileRequestDTO {
    @NotBlank
    @Size(min = 1, max = 255)
    private String name; // 이름

    @NotBlank
    @Size(min = 1, max = 255)
    private String url; // url

    @NotNull private FileType type; // 할인 분양가
}
