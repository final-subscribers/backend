package subscribers.clearbunyang.domain.consultation.dto.adminPropertyConsultation.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SideBarPendingResponse {

    private Long id; // 매물 id

    private String name; // 매물 이름
}
