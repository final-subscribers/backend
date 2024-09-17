package subscribers.clearbunyang.testfixtures;


import java.time.LocalDate;
import subscribers.clearbunyang.domain.property.model.request.MemberConsultationRequestDTO;

public class MemberConsultationRequestDTOFixture {
    public static MemberConsultationRequestDTO createDefault() {
        return new MemberConsultationRequestDTO(
                "bom", "01012345678", LocalDate.now().plusDays(2), "Sample consultation message");
    }

    public static MemberConsultationRequestDTO createCustom(
            String name, String phoneNumber, LocalDate date, String message) {
        return new MemberConsultationRequestDTO(name, phoneNumber, date, message);
    }
}
