package subscribers.clearbunyang.domain.property.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.domain.file.entity.enums.FileType;
import subscribers.clearbunyang.domain.file.model.FileRequestDTO;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordName;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordType;
import subscribers.clearbunyang.domain.property.entity.enums.PropertyType;
import subscribers.clearbunyang.domain.property.entity.enums.SalesType;
import subscribers.clearbunyang.domain.property.model.request.AreaRequestDTO;
import subscribers.clearbunyang.domain.property.model.request.KeywordRequestDTO;
import subscribers.clearbunyang.domain.property.model.request.PropertyRequestDTO;
import subscribers.clearbunyang.domain.property.service.PropertyService;
import subscribers.clearbunyang.global.config.SecurityConfig;
import subscribers.clearbunyang.global.token.JwtTokenProcessor;
import subscribers.clearbunyang.security.annotation.WithMockCustomAdmin;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@DisplayName("AdminPropertyController-통합 테스트")
@Transactional
public class AdminPropertyControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private PropertyService propertyService;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private JwtTokenProcessor jwtTokenProcessor;

    @DisplayName("물건 등록 테스트")
    @Test
    @WithMockCustomAdmin
    public void addProperty1() throws Exception {
        PropertyRequestDTO requestDTO = createTestPropertyRequestDTO();

        mockMvc.perform(
                        post("/api/admin/properties")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(requestDTO))
                                .with(csrf()))
                .andExpect(status().isOk());
    }

    private PropertyRequestDTO createTestPropertyRequestDTO() {
        List<AreaRequestDTO> areas = new ArrayList<>();
        areas.add(new AreaRequestDTO(60, 50000, 45000, 10));
        areas.add(new AreaRequestDTO(80, 60000, 55000, 8));

        List<FileRequestDTO> files = new ArrayList<>();
        files.add(
                new FileRequestDTO(
                        "property_image.jpg",
                        "https://example.com/image.jpg",
                        FileType.PROPERTY_IMAGE));
        files.add(
                new FileRequestDTO(
                        "supply_information.pdf",
                        "https://example.com/supply.pdf",
                        FileType.SUPPLY_INFORMATION));

        List<KeywordRequestDTO> keywords = new ArrayList<>();
        keywords.add(
                new KeywordRequestDTO(KeywordName.CASH_PAYMENT, KeywordType.BENEFIT, true, 100));
        keywords.add(
                new KeywordRequestDTO(
                        KeywordName.SUBWAY,
                        KeywordType.INFRA,
                        true,
                        Map.of("station", "Gangnam", "distance", "500m")));

        return new PropertyRequestDTO(
                "푸르지오 아파트",
                "현대건설",
                "현대자이",
                100,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31),
                PropertyType.APARTMENT,
                SalesType.PUBLIC_SALE,
                "서울시 강남구 역삼동 123",
                "서울시",
                "강남구",
                "역삼동",
                "푸르지오 아파트",
                "서울시 강남구 역삼동 456",
                "01012345678",
                null,
                "https://kakao.com/channel",
                areas,
                files,
                keywords);
    }
}
