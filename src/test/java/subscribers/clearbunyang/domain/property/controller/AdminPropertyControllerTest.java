package subscribers.clearbunyang.domain.property.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.model.request.AreaRequestDTO;
import subscribers.clearbunyang.domain.property.model.request.PropertyRequestDTO;
import subscribers.clearbunyang.domain.property.service.PropertyService;
import subscribers.clearbunyang.domain.property.testfixtures.PropertyRequestDTOFixture;
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
        PropertyRequestDTO requestDTO = PropertyRequestDTOFixture.createDefault();
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
        PropertyRequestDTO requestDTO = PropertyRequestDTOFixture.createDefault();
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
}
