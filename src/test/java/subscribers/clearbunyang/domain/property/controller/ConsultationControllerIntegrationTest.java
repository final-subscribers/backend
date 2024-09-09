package subscribers.clearbunyang.domain.property.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.domain.property.model.response.PropertyDetailsResponseDTO;
import subscribers.clearbunyang.domain.property.service.PropertyService;
import subscribers.clearbunyang.global.config.SecurityConfig;
import subscribers.clearbunyang.global.token.JwtTokenProcessor;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@DisplayName("통합 테스트")
@Transactional
@TestPropertySource(properties = "spring.jpa.properties.hibernate.default_batch_fetch_size=100")
public class ConsultationControllerIntegrationTest {
    @Autowired private MockMvc mockMvc;

    @MockBean private JwtTokenProcessor jwtTokenProcessor;
    @Autowired private PropertyService propertyService;

    @DisplayName("상담 수정 동시성 테스트")
    @Test
    public void consultation() throws Exception {

        //        mockMvc.perform(
        //                        put("/api/admin/consultations/{adminConsultationId}", 175L)
        //                                .contentType("application/json")
        //                                .param("consultant", "asa")
        //                                .with(csrf()))
        //                .andExpect(status().isOk());
        PropertyDetailsResponseDTO propertyDetails = propertyService.getPropertyDetails(28L, null);
        System.out.println(propertyDetails);
    }
}
