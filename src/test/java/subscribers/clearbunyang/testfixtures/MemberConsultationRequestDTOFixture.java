package subscribers.clearbunyang.testfixtures;


import java.time.LocalDate;
import subscribers.clearbunyang.domain.property.dto.request.MemberConsultationRequest;

public class MemberConsultationRequestDTOFixture {
    public static MemberConsultationRequest createDefault() {
        return new MemberConsultationRequest(
                "bom", "01012345678", LocalDate.now().plusDays(2), "Sample consultation message");
    }

    public static MemberConsultationRequest createCustom(
            String name, String phoneNumber, LocalDate date, String message) {
        return new MemberConsultationRequest(name, phoneNumber, date, message);
    }
}
