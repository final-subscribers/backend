package subscribers.clearbunyang.domain.property.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.domain.consultation.entity.MemberConsultation;
import subscribers.clearbunyang.domain.property.entity.Area;
import subscribers.clearbunyang.domain.property.entity.Keyword;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.model.request.KeywordRequestDTO;
import subscribers.clearbunyang.domain.property.repository.AreaRepository;
import subscribers.clearbunyang.domain.property.repository.KeywordRepository;
import subscribers.clearbunyang.testfixtures.ConsultationRequestDTOFixture;
import subscribers.clearbunyang.testfixtures.PropertyRequestDTOFixture;

@SpringBootTest
@DisplayName("PropertyService-통합 테스트")
@Transactional
public class PropertyServiceIntegrationTest {

    @Autowired private PropertyService propertyService;

    @Autowired private AreaRepository areaRepository;

    @Autowired private KeywordRepository keywordRepository;

    private Property savedProperty;

    @BeforeEach
    void setUp() {
        this.savedProperty =
                propertyService.saveProperty(PropertyRequestDTOFixture.createDefault(), 1L);
    }

    @Test
    @DisplayName("물건 저장 테스트")
    void saveProperty() {
        List<Keyword> keywords = keywordRepository.findByPropertyId(savedProperty.getId());
        List<Area> areas = areaRepository.findByPropertyId(savedProperty.getId());
        List<KeywordRequestDTO> searchEnabledKeywords =
                PropertyRequestDTOFixture.createDefault().getKeywords().stream()
                        .filter(keyword -> keyword.getSearchEnabled() == true)
                        .collect(Collectors.toList());

        for (int i = 0; i < searchEnabledKeywords.size(); i++) {
            assertThat(keywords.get(i).getName()).isEqualTo(searchEnabledKeywords.get(i).getName());
            assertThat(keywords.get(i).getType()).isEqualTo(searchEnabledKeywords.get(i).getType());
        }
        assertThat(keywords.get(searchEnabledKeywords.size()).getName()).isNull();
        assertThat(keywords.get(searchEnabledKeywords.size()).getType()).isNull();

        assertThat(areas).hasSize(PropertyRequestDTOFixture.createDefault().getAreas().size());
    }

    @Test
    @DisplayName("상담 신청 테스트-비회원일 경우")
    void saveConsultation1() {
        MemberConsultation memberConsultation =
                propertyService.saveConsultation(
                        savedProperty.getId(), ConsultationRequestDTOFixture.createDefault(), null);

        assertThat(memberConsultation.getAdminConsultation()).isNotNull();
        assertThat(memberConsultation.getMember()).isNull();
        assertThat(memberConsultation.getPreferredAt())
                .isEqualTo(ConsultationRequestDTOFixture.createDefault().getPreferredAt());
    }

    @Test
    @DisplayName("상담 신청 테스트-회원일 경우")
    void saveConsultation2() {
        MemberConsultation memberConsultation =
                propertyService.saveConsultation(
                        savedProperty.getId(), ConsultationRequestDTOFixture.createDefault(), 1L);

        assertThat(memberConsultation.getAdminConsultation()).isNotNull();
        assertThat(memberConsultation.getMember()).isNotNull();
        assertThat(memberConsultation.getPreferredAt())
                .isEqualTo(ConsultationRequestDTOFixture.createDefault().getPreferredAt());
    }
}
