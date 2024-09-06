package subscribers.clearbunyang.domain.property.model.response;


import java.util.List;
import lombok.*;
import subscribers.clearbunyang.domain.property.model.MyPropertyDTO;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPropertiesResponseDTO<T extends MyPropertyDTO> {

    private int allProperties; // 총 등록한 매물 수
    private List<T> properties; // 한 페이지당 리턴할 매물 리스트
}
