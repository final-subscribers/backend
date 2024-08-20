package subscribers.clearbunyang.domain.property.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribers.clearbunyang.domain.file.entity.enums.FileType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileDTO {
    private String name; // 이름
    private String url; // url
    private FileType type; // 할인 분양가
}
