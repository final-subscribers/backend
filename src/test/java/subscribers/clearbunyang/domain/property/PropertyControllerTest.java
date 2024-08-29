package subscribers.clearbunyang.domain.property;

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
import org.springframework.test.web.servlet.MockMvc;
import subscribers.clearbunyang.domain.file.entity.enums.FileType;
import subscribers.clearbunyang.domain.file.model.FileDTO;
import subscribers.clearbunyang.domain.property.controller.PropertyController;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.entity.enums.PropertyType;
import subscribers.clearbunyang.domain.property.entity.enums.SalesType;
import subscribers.clearbunyang.domain.property.model.AreaDTO;
import subscribers.clearbunyang.domain.property.model.KeywordDTO;
import subscribers.clearbunyang.domain.property.model.PropertyRequestDTO;
import subscribers.clearbunyang.domain.property.service.PropertyService;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;
import subscribers.clearbunyang.security.annotation.WithMockCustomAdmin;

@WebMvcTest(PropertyController.class)
public class PropertyControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private PropertyService propertyService;
    @Autowired private ObjectMapper objectMapper;

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
        requestDTO.getAreas().add(new AreaDTO(60, 50000, 60000, 10));
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
        List<AreaDTO> areas = new ArrayList<>();
        areas.add(new AreaDTO(60, 50000, 45000, 10));
        areas.add(new AreaDTO(80, 60000, 55000, 8));

        List<FileDTO> files = new ArrayList<>();
        files.add(
                new FileDTO(
                        "property_image.jpg",
                        "https://example.com/image.jpg",
                        FileType.PROPERTY_IMAGE));
        files.add(
                new FileDTO(
                        "supply_information.pdf",
                        "https://example.com/supply.pdf",
                        FileType.SUPPLY_INFORMATION));

        List<KeywordDTO> keywords = new ArrayList<>();
        keywords.add(new KeywordDTO("CASH_PAYMENT", "BENEFIT", true, 100));
        keywords.add(
                new KeywordDTO(
                        "SUBWAY", "INFRA", true, Map.of("station", "Gangnam", "distance", "500m")));

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
                "https://example.com",
                "https://kakao.com/channel",
                areas,
                files,
                keywords);
    }
}
