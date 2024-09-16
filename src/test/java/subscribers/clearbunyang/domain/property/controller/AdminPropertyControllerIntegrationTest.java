package subscribers.clearbunyang.domain.property.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.global.config.SecurityConfig;
import subscribers.clearbunyang.security.AuthenticationFilterMocking;
import subscribers.clearbunyang.security.annotation.WithMockCustomAdmin;
import subscribers.clearbunyang.security.annotation.WithMockCustomMember;
import subscribers.clearbunyang.testfixtures.PropertyRequestDTOFixture;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@DisplayName("AdminPropertyController-통합 테스트")
@Transactional
public class AdminPropertyControllerIntegrationTest extends AuthenticationFilterMocking {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    @WithMockCustomAdmin
    @DisplayName("물건등록-admin일때")
    public void 물건_등록_테스트1() throws Exception {
        mockMvc.perform(
                        post("/api/admin/properties")
                                .contentType("application/json")
                                .content(
                                        objectMapper.writeValueAsString(
                                                PropertyRequestDTOFixture.createDefault()))
                                .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockCustomMember
    @DisplayName("물건등록-member일때")
    public void 물건_등록_테스트2() throws Exception {
        mockMvc.perform(
                        post("/api/admin/properties")
                                .contentType("application/json")
                                .content(
                                        objectMapper.writeValueAsString(
                                                PropertyRequestDTOFixture.createDefault()))
                                .with(csrf()))
                .andExpect(status().is4xxClientError());
    }
}
