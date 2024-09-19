package subscribers.clearbunyang.domain.consultation.model.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode // 중복 제거
public class ConsultantResponse {

    private String consultant;

    public static ConsultantResponse toDto(String consultant) {
        return ConsultantResponse.builder().consultant(consultant).build();
    }
}
