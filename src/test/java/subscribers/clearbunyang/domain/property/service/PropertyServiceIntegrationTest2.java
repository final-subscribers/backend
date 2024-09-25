package subscribers.clearbunyang.domain.property.service;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.domain.auth.entity.Admin;
import subscribers.clearbunyang.domain.auth.repository.AdminRepository;
import subscribers.clearbunyang.domain.property.dto.response.MyPropertyCardResponse;
import subscribers.clearbunyang.domain.property.dto.response.MyPropertyTableResponse;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.global.dto.PagedDto;
import subscribers.clearbunyang.testfixtures.AdminRegisterFixture;
import subscribers.clearbunyang.testfixtures.MemberConsultationRequestDTOFixture;
import subscribers.clearbunyang.testfixtures.PropertySaveRequestDTOFixture;

@SpringBootTest
@DisplayName("PropertyService-통합 테스트2")
@Transactional
public class PropertyServiceIntegrationTest2 {

    @Autowired private PropertyService propertyService;
    @Autowired private AdminRepository adminRepository;
    @Autowired private EntityManager entityManager;
    private List<Property> savedProperties;
    private Admin savedAdmin;

    @BeforeEach
    void setUp() {
        savedAdmin = adminRepository.save(AdminRegisterFixture.createDefault());

        savedProperties = new ArrayList<>();
        savedProperties.add(
                propertyService.saveProperty(
                        PropertySaveRequestDTOFixture.createCustom("테스트1", "https://테스트1.png"),
                        savedAdmin.getId()));
        savedProperties.add(
                propertyService.saveProperty(
                        PropertySaveRequestDTOFixture.createCustom("테스트2", "https://테스트2.png"),
                        savedAdmin.getId()));
        savedProperties.add(
                propertyService.saveProperty(
                        PropertySaveRequestDTOFixture.createCustom("테스트3", "https://테스트3.png"),
                        savedAdmin.getId()));
        savedProperties.add(
                propertyService.saveProperty(
                        PropertySaveRequestDTOFixture.createCustom("테스트4", "https://테스트4.png"),
                        savedAdmin.getId()));
        savedProperties.add(
                propertyService.saveProperty(
                        PropertySaveRequestDTOFixture.createCustom("테스트5", "https://테스트5.png"),
                        savedAdmin.getId()));
        savedProperties.add(
                propertyService.saveProperty(
                        PropertySaveRequestDTOFixture.createCustom("테스트6", "https://테스트6.png"),
                        savedAdmin.getId()));

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("매물 관리-첫번째 페이지네이션 조회")
    void getCard() {
        int currentPage = 1;
        int size = 3;
        PagedDto<MyPropertyCardResponse> cards =
                propertyService.getCards(currentPage, size, savedAdmin.getId());

        assertThat(cards.getTotalPages()).isEqualTo((savedProperties.size() + size - 1) / size);
        int startIdx = savedProperties.size() - 1 - (currentPage * size);
        for (int i = 0; i < cards.getContents().size(); i++) {
            assertThat(cards.getContents().get(i).getName())
                    .isEqualTo(savedProperties.get(startIdx - i).getName());
            assertThat(cards.getContents().get(i).getImageUrl())
                    .isEqualTo(savedProperties.get(startIdx - i).getImageUrl());
        }
    }

    @Test
    @DisplayName("매물 관리-두번째 페이지네이션 조회")
    void getTable() {
        propertyService.saveConsultation(
                savedProperties.get(0).getId(),
                MemberConsultationRequestDTOFixture.createDefault(),
                null);

        int currentPage = 1;
        int size = 3;
        PagedDto<MyPropertyTableResponse> tables =
                propertyService.getTables(currentPage, size, savedAdmin.getId());

        assertThat(tables.getTotalPages()).isEqualTo((savedProperties.size() + size - 1) / size);
        int startIdx = savedProperties.size() - 1 - (currentPage * size);
        for (int i = 0; i < tables.getContents().size(); i++) {
            assertThat(tables.getContents().get(i).getName())
                    .isEqualTo(savedProperties.get(startIdx - i).getName());
        }
        assertThat(tables.getContents().get(startIdx).getConsultationPendingCount()).isEqualTo(1);
    }
}
