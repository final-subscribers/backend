package subscribers.clearbunyang.testdata;


import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.domain.consultation.dto.adminConsultation.request.ConsultRequest;
import subscribers.clearbunyang.domain.consultation.entity.MemberConsultation;
import subscribers.clearbunyang.domain.consultation.entity.enums.Status;
import subscribers.clearbunyang.domain.consultation.service.AdminConsultationService;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.repository.PropertyRepository;

@SpringBootTest
@Disabled("테스트 데이터 삽입시에만 사용")
public class CompletedConsultationInsert {

    @Autowired private PropertyRepository propertyRepository;
    @Autowired private AdminConsultationService adminConsultationService;
    @Autowired private EntityManager entityManager;

    /** lms에서 비회원/member가 추가한 상담을 상담완료로 바꾸는 테스트 데이터 삽입 */
    @Test
    @Transactional
    @Rollback(false)
    void saveMemberConsultation() {
        List<Property> properties = propertyRepository.findAll();

        for (Property property : properties) {
            List<MemberConsultation> pendingConsultations =
                    property.getMemberConsultations().stream()
                            .filter(mc -> mc.getStatus() == Status.PENDING)
                            .collect(Collectors.toList());
            if (pendingConsultations.isEmpty()) continue;

            Random random = new Random();
            int randomCount =
                    random.nextInt(Math.min(pendingConsultations.size(), 6)); // 최대 pending 수까지 제한

            for (int i = 0; i < randomCount; i++) {
                MemberConsultation selectedConsultation = pendingConsultations.get(i);
                ConsultRequest requestDTO =
                        ConsultRequest.builder()
                                .status(Status.COMPLETED)
                                .tier(AdminConsultationInsert.generateRandomTier())
                                .consultantMessage("상담 완료되었습니다.")
                                .build();

                adminConsultationService.registerConsultant(
                        selectedConsultation.getAdminConsultation().getId(),
                        AdminConsultationInsert.generateRandomConsultant());
                entityManager.flush();
                entityManager.clear();
                adminConsultationService.createAdminConsult(
                        selectedConsultation.getId(), requestDTO);
            }
        }
    }
}
