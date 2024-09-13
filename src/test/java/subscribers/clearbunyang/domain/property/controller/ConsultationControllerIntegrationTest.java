package subscribers.clearbunyang.domain.property.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.List;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.domain.file.entity.File;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.model.response.PropertyDetailsResponseDTO;
import subscribers.clearbunyang.domain.property.repository.PropertyRepository;
import subscribers.clearbunyang.domain.property.service.PropertyService;
import subscribers.clearbunyang.global.config.SecurityConfig;
import subscribers.clearbunyang.global.token.JwtTokenProcessor;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@DisplayName("통합 테스트")
@Transactional
public class ConsultationControllerIntegrationTest {
    @Autowired private MockMvc mockMvc;

    @MockBean private JwtTokenProcessor jwtTokenProcessor;
    @Autowired private PropertyService propertyService;
    @Autowired private PropertyRepository propertyRepository;

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
        Property property = propertyRepository.findById(28L).get();
        List<File> files = property.getFiles();
        System.out.println(files.get(0).getName());
    }
}
