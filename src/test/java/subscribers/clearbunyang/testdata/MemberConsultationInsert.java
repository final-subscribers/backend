package subscribers.clearbunyang.testdata;


import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import subscribers.clearbunyang.domain.consultation.entity.MemberConsultation;
import subscribers.clearbunyang.domain.property.dto.request.MemberConsultationRequest;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.repository.PropertyRepository;
import subscribers.clearbunyang.domain.property.service.PropertyService;
import subscribers.clearbunyang.testfixtures.MemberConsultationRequestDTOFixture;

@SpringBootTest
@Disabled("테스트 데이터 삽입시에만 사용")
public class MemberConsultationInsert {

    @Autowired private PropertyRepository propertyRepository;
    @Autowired private PropertyService propertyService;

    /** lms에서 비회원/member가 상담 추가하는 테스트 데이터 삽입 */
    @Test
    void saveMemberConsultation() {
        List<Property> properties = propertyRepository.findAll();
        List<Long> selected = List.of(856L, 857L, 858L, 882L, 884L, 855L);
        for (int i = 0; i < selected.size(); i++) {
            Property property = propertyRepository.findPropertyById(selected.get(i));
            for (int j = 0; j < 10; j++) {
                MemberConsultationRequest requestDTO =
                        MemberConsultationRequestDTOFixture.createCustom(
                                RandomKoreanNameGenerator.generateRandomName(),
                                RandomPhoneNumberGenerator.generatePhoneNumber(),
                                RandomDateGenerator.genearateDate(
                                        LocalDate.of(2024, 01, 01), LocalDate.of(2024, 12, 01)),
                                "상담 신청합니다!");
                MemberConsultation memberConsultation =
                        propertyService.saveConsultation(property.getId(), requestDTO, null);
            }
        }
    }
}
