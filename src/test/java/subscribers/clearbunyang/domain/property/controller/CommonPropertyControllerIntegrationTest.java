package subscribers.clearbunyang.domain.property.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.domain.auth.entity.Admin;
import subscribers.clearbunyang.domain.auth.repository.AdminRepository;
import subscribers.clearbunyang.domain.property.dto.request.MemberConsultationRequest;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.service.PropertyService;
import subscribers.clearbunyang.global.config.SecurityConfig;
import subscribers.clearbunyang.security.AuthenticationFilterMocking;
import subscribers.clearbunyang.security.annotation.WithMockCustomMember;
import subscribers.clearbunyang.testfixtures.AdminRegisterFixture;
import subscribers.clearbunyang.testfixtures.MemberConsultationRequestDTOFixture;
import subscribers.clearbunyang.testfixtures.PropertySaveRequestDTOFixture;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@DisplayName("CommonPropertyController-통합 테스트")
@Transactional
public class CommonPropertyControllerIntegrationTest extends AuthenticationFilterMocking {
    @Autowired private MockMvc mockMvc;

    @Autowired private PropertyService propertyService;
    @Autowired private AdminRepository adminRepository;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private EntityManager entityManager;

    private Admin savedAdmin;
    private Property savedProperty;

    @BeforeEach
    @Order(Integer.MAX_VALUE)
    void save() {
        savedAdmin = adminRepository.save(AdminRegisterFixture.createDefault());
        savedProperty =
                propertyService.saveProperty(
                        PropertySaveRequestDTOFixture.createDefault(), savedAdmin.getId());
    }

    @DisplayName("상담 등록 테스트: 로그인 안한 사용자")
    @Test
    public void addConsultation1() throws Exception {
        MemberConsultationRequest requestDTO = MemberConsultationRequestDTOFixture.createDefault();

        mockMvc.perform(
                        post(
                                        "/api/common/properties/{propertyId}/consultation",
                                        savedProperty.getId())
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(requestDTO))
                                .with(csrf()))
                .andExpect(status().isOk());
    }

    @DisplayName("상담 등록 테스트: member일때")
    @Test
    @WithMockCustomMember
    public void addConsultation2() throws Exception {
        MemberConsultationRequest requestDTO = MemberConsultationRequestDTOFixture.createDefault();

        mockMvc.perform(
                        post(
                                        "/api/member/properties/{propertyId}/consultation",
                                        savedProperty.getId())
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(requestDTO))
                                .with(csrf()))
                .andExpect(status().isOk());
    }

    @DisplayName("매물 읽어오기")
    @Test
    public void getProperty() throws Exception {
        MemberConsultationRequest requestDTO = MemberConsultationRequestDTOFixture.createDefault();
        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(
                        get("/api/common/properties/{propertyId}", savedProperty.getId())
                                .contentType("application/json")
                                .with(csrf()))
                .andDo(System.out::println);
    }
}
