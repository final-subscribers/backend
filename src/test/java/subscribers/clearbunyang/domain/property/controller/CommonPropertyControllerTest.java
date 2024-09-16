package subscribers.clearbunyang.domain.property.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import subscribers.clearbunyang.domain.property.model.request.ConsultationRequestDTO;
import subscribers.clearbunyang.domain.property.service.PropertyService;
import subscribers.clearbunyang.global.config.SecurityConfig;
import subscribers.clearbunyang.security.AuthenticationFilterMocking;
import subscribers.clearbunyang.security.annotation.WithMockCustomAdmin;
import subscribers.clearbunyang.security.annotation.WithMockCustomMember;
import subscribers.clearbunyang.testfixtures.ConsultationRequestDTOFixture;

@WebMvcTest(CommonPropertyController.class)
@Import(SecurityConfig.class)
@DisplayName("CommonPropertyController-단위 테스트")
public class CommonPropertyControllerTest extends AuthenticationFilterMocking {
    @Autowired MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private PropertyService propertyService;
    private ConsultationRequestDTO requestDTO;

    @BeforeEach
    @Order(Integer.MAX_VALUE)
    void save() {
        requestDTO = ConsultationRequestDTOFixture.createDefault();
    }

    @DisplayName("상담 등록 테스트: 로그인 안한 사용자")
    @Test
    public void addConsultation1() throws Exception {
        mockMvc.perform(
                        post("/api/common/properties/{propertyId}/consultation", anyLong())
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(requestDTO))
                                .with(csrf()))
                .andExpect(status().isOk());
    }

    @DisplayName("상담 등록 테스트: member일때")
    @Test
    @WithMockCustomMember
    public void addConsultation2() throws Exception {
        mockMvc.perform(
                        post("/api/common/properties/{propertyId}/consultation", anyLong())
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(requestDTO))
                                .with(csrf()))
                .andExpect(status().isOk());
    }

    @DisplayName("상담 등록 테스트: admin일때")
    @Test
    @WithMockCustomAdmin
    public void addConsultation3() throws Exception {
        mockMvc.perform(
                        post("/api/common/properties/{propertyId}/consultation", anyLong())
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(requestDTO))
                                .with(csrf()))
                .andExpect(status().isForbidden());
    }
}
