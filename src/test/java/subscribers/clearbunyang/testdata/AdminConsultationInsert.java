package subscribers.clearbunyang.testdata;


import java.time.LocalDate;
import java.util.Random;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import subscribers.clearbunyang.domain.consultation.entity.enums.Medium;
import subscribers.clearbunyang.domain.consultation.entity.enums.Status;
import subscribers.clearbunyang.domain.consultation.entity.enums.Tier;
import subscribers.clearbunyang.domain.consultation.model.request.NewCustomerAdditionRequest;
import subscribers.clearbunyang.domain.consultation.service.PropertiesService;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.repository.PropertyRepository;

@SpringBootTest
@Disabled("테스트 데이터 삽입시에만 사용")
public class AdminConsultationInsert {

    @Autowired private PropertyRepository propertyRepository;
    @Autowired private PropertiesService propertiesService;

    /** admin이 신규 상담 추가하는 테스트 데이터 삽입 */
    @Test
    void saveMemberConsultation() {
        Property properties = propertyRepository.findPropertyById(855L);

        for (int i = 0; i < 20; i++) {
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
            propertiesService.createNewCustomerAddition(855L, requestDTO);
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
