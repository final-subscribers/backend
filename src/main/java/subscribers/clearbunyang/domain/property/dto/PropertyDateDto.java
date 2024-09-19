package subscribers.clearbunyang.domain.property.dto;


import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyDateDto {

    private Long id;

    private String name;

    private LocalDate endDate;

    private LocalDate startDate;
}
