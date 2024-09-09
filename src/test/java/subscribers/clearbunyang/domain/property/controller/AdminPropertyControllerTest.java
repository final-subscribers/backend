package subscribers.clearbunyang.domain.property.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import subscribers.clearbunyang.domain.file.entity.enums.FileType;
import subscribers.clearbunyang.domain.file.model.FileRequestDTO;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordName;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordType;
import subscribers.clearbunyang.domain.property.entity.enums.PropertyType;
import subscribers.clearbunyang.domain.property.entity.enums.SalesType;
import subscribers.clearbunyang.domain.property.model.request.AreaRequestDTO;
import subscribers.clearbunyang.domain.property.model.request.KeywordRequestDTO;
import subscribers.clearbunyang.domain.property.model.request.PropertyRequestDTO;
import subscribers.clearbunyang.domain.property.service.PropertyService;
import subscribers.clearbunyang.global.config.SecurityConfig;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;
import subscribers.clearbunyang.global.token.JwtTokenProcessor;
import subscribers.clearbunyang.security.annotation.WithMockCustomAdmin;

@WebMvcTest(AdminPropertyController.class)
@Import(SecurityConfig.class)
@DisplayName("admin-property-controller")
public class AdminPropertyControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private PropertyService propertyService;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private JwtTokenProcessor jwtTokenProcessor;

    @DisplayName("물건 등록 테스트")
    @Test
    @WithMockCustomAdmin
    public void addProperty1() throws Exception {
        PropertyRequestDTO requestDTO = createTestPropertyRequestDTO();
        Property mockProperty = new Property();
        when(propertyService.saveProperty(any(PropertyRequestDTO.class), any(Long.class)))
                .thenReturn(mockProperty);

        mockMvc.perform(
                        post("/api/admin/properties")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(requestDTO))
                                .with(csrf()))
                .andExpect(status().isOk());
    }

    @DisplayName("물건 등록 테스트: request validation 실패")
    @Test
    @WithMockCustomAdmin
    public void addProperty2() throws Exception {
        PropertyRequestDTO requestDTO = createTestPropertyRequestDTO();
        requestDTO.getAreas().add(new AreaRequestDTO(60, 50000, 60000, 10));
        Property mockProperty = new Property();

        when(propertyService.saveProperty(any(PropertyRequestDTO.class), any(Long.class)))
                .thenReturn(mockProperty);

        mockMvc.perform(
                        post("/api/admin/properties")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(requestDTO))
                                .with(csrf()))
                .andExpect(status().is(ErrorCode.BAD_REQUEST.getStatus()))
                .andExpect(
                        jsonPath("$.result.resultMessage")
                                .value(ErrorCode.BAD_REQUEST.getMessage()));
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
