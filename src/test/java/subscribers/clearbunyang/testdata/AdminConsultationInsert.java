package subscribers.clearbunyang.testdata;


import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import subscribers.clearbunyang.domain.consultation.dto.adminPropertyConsultation.request.NewCustomerAdditionRequest;
import subscribers.clearbunyang.domain.consultation.entity.enums.Medium;
import subscribers.clearbunyang.domain.consultation.entity.enums.Status;
import subscribers.clearbunyang.domain.consultation.entity.enums.Tier;
import subscribers.clearbunyang.domain.consultation.service.AdminPropertyConsultationService;
import subscribers.clearbunyang.domain.property.repository.PropertyRepository;

@SpringBootTest
@Disabled("테스트 데이터 삽입시에만 사용")
public class AdminConsultationInsert {

    @Autowired private PropertyRepository propertyRepository;
    @Autowired private AdminPropertyConsultationService adminPropertyConsultationService;

    /** admin이 신규 상담 추가하는 테스트 데이터 삽입 */
    @Test
    void saveMemberConsultation() {
        List<Long> selected = List.of(43L, 44L, 45L, 46L, 48L, 49L);
        for (int j = 0; j < selected.size(); j++) {
            for (int i = 0; i < 5; i++) {
                Status randomStatus = generateRandomStatus();
                NewCustomerAdditionRequest requestDTO;
                if (randomStatus == Status.PENDING) {
                    requestDTO =
                            NewCustomerAdditionRequest.builder()
                                    .name(RandomKoreanNameGenerator.generateRandomName())
                                    .phoneNumber(RandomPhoneNumberGenerator.generatePhoneNumber())
                                    .preferredAt(LocalDate.now().plusDays(1))
                                    .consultant(generateRandomConsultant())
                                    .tier(null)
                                    .status(Status.PENDING)
                                    .medium(Medium.PHONE)
                                    .build();
                } else {
                    requestDTO =
                            NewCustomerAdditionRequest.builder()
                                    .name(RandomKoreanNameGenerator.generateRandomName())
                                    .phoneNumber(RandomPhoneNumberGenerator.generatePhoneNumber())
                                    .preferredAt(LocalDate.now().plusDays(1))
                                    .consultant(generateRandomConsultant())
                                    .tier(generateRandomTier())
                                    .status(Status.COMPLETED)
                                    .medium(Medium.PHONE)
                                    .build();
                }
                adminPropertyConsultationService.createNewCustomerAddition(
                        selected.get(j), requestDTO);
            }
        }
    }

    private static Status generateRandomStatus() {
        Status[] statuses = Status.values();
        return statuses[new Random().nextInt(statuses.length)];
    }

    public static Tier generateRandomTier() {
        Tier[] tiers = Tier.values();
        return tiers[new Random().nextInt(tiers.length)];
    }

    public static String generateRandomConsultant() {
        String[] values = {"a1-1", "a1-2", "a1-3", "a1-4", "a1-5"};
        Random random = new Random();
        int randomIndex = random.nextInt(values.length);
        return values[randomIndex];
    }
}
