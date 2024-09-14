package subscribers.clearbunyang.domain.property.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.domain.property.entity.Area;
import subscribers.clearbunyang.domain.property.entity.Keyword;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordName;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordType;
import subscribers.clearbunyang.domain.property.repository.AreaRepository;
import subscribers.clearbunyang.domain.property.repository.KeywordRepository;
import subscribers.clearbunyang.domain.property.testfixtures.PropertyRequestDTOFixture;

@SpringBootTest
@DisplayName("PropertyService-통합 테스트")
@Transactional
public class PropertyServiceIntegrationTest {

    @Autowired private PropertyService propertyService;

    @Autowired private AreaRepository areaRepository;

    @Autowired private KeywordRepository keywordRepository;

    @Test
    @DisplayName("물건 저장 테스트")
    void saveProperty() {
        Property savedProperty =
                propertyService.saveProperty(PropertyRequestDTOFixture.createDefault(), 1L);
        List<Keyword> keywords = keywordRepository.findByPropertyId(savedProperty.getId());
        List<Area> areas = areaRepository.findByPropertyId(savedProperty.getId());

        assertThat(keywords).hasSize(3);
        assertThat(keywords.get(0).getName()).isEqualTo(KeywordName.CASH_PAYMENT);
        assertThat(keywords.get(0).getType()).isEqualTo(KeywordType.BENEFIT);
        assertThat(keywords.get(1).getName()).isEqualTo(KeywordName.SUBWAY);
        assertThat(keywords.get(1).getType()).isEqualTo(KeywordType.INFRA);
        assertThat(keywords.get(2).getName()).isNull();
        assertThat(keywords.get(2).getType()).isNull();

        assertThat(areas).hasSize(2);
    }
}
