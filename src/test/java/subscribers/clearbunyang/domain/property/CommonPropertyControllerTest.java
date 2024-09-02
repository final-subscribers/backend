package subscribers.clearbunyang.domain.property;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import subscribers.clearbunyang.domain.property.controller.CommonPropertyController;
import subscribers.clearbunyang.domain.property.model.ConsultationRequestDTO;
import subscribers.clearbunyang.domain.property.service.PropertyService;
import subscribers.clearbunyang.global.config.SecurityConfig;
import subscribers.clearbunyang.global.token.JwtTokenProcessor;
import subscribers.clearbunyang.security.annotation.WithMockCustomAdmin;
import subscribers.clearbunyang.security.annotation.WithMockCustomMember;

@WebMvcTest(CommonPropertyController.class)
@Import(SecurityConfig.class)
public class CommonPropertyControllerTest {
    @Autowired MockMvc mockMvc;

    @MockBean private PropertyService propertyService;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private JwtTokenProcessor jwtTokenProcessor;

    @DisplayName("상담 등록 테스트: 로그인 안한 사용자")
    @Test
    public void addConsultation1() throws Exception {
        ConsultationRequestDTO requestDTO = createTestConsultationRequestDTO();

        mockMvc.perform(
                        post("/api/common/properties/{propertyId}/consultation", 1L)
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(requestDTO))
                                .with(csrf()))
                .andExpect(status().isOk());
    }

    @DisplayName("상담 등록 테스트: member일때")
    @Test
    @WithMockCustomMember
    public void addConsultation2() throws Exception {
        ConsultationRequestDTO requestDTO = createTestConsultationRequestDTO();

        mockMvc.perform(
                        post("/api/common/properties/{propertyId}/consultation", 1L)
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(requestDTO))
                                .with(csrf()))
                .andExpect(status().isOk());
    }

    @DisplayName("상담 등록 테스트: admin일때")
    @Test
    @WithMockCustomAdmin
    public void addConsultation3() throws Exception {
        ConsultationRequestDTO requestDTO = createTestConsultationRequestDTO();

        mockMvc.perform(
                        post("/api/common/properties/{propertyId}/consultation", 1L)
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(requestDTO))
                                .with(csrf()))
                .andExpect(status().isForbidden());
    }

    private ConsultationRequestDTO createTestConsultationRequestDTO() {
        return new ConsultationRequestDTO(
                "bom", "01012345678", LocalDate.now(), "Sample consultation message");
    }
}
