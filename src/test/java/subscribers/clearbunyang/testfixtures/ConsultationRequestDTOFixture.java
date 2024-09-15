package subscribers.clearbunyang.testfixtures;


import java.time.LocalDate;
import subscribers.clearbunyang.domain.property.model.request.ConsultationRequestDTO;

public class ConsultationRequestDTOFixture {
    public static ConsultationRequestDTO createDefault() {
        return new ConsultationRequestDTO(
                "bom", "01012345678", LocalDate.now().plusDays(2), "Sample consultation message");
    }
}
