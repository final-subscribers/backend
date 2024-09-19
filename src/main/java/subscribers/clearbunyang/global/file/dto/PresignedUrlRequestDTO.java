package subscribers.clearbunyang.global.file.dto;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PresignedUrlRequestDTO {
    private List<UploadFileDTO> files;
}
