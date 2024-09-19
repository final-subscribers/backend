package subscribers.clearbunyang.global.file.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.global.file.entity.File;
import subscribers.clearbunyang.global.file.entity.enums.FileType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileResponseDTO {
    private String name; // 이름

    private String url; // url

    private FileType type; // 할인 분양가

    public static FileResponseDTO toDTO(File file) {
        return FileResponseDTO.builder()
                .name(file.getName())
                .url(file.getLink())
                .type(file.getType())
                .build();
    }
}
