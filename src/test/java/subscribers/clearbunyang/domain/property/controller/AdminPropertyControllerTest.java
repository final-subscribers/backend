package subscribers.clearbunyang.domain.property.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import subscribers.clearbunyang.domain.property.dto.request.AreaRequest;
import subscribers.clearbunyang.domain.property.dto.request.PropertySaveRequest;
import subscribers.clearbunyang.domain.property.dto.request.PropertyUpdateRequest;
import subscribers.clearbunyang.domain.property.dto.response.MyPropertyCardResponse;
import subscribers.clearbunyang.domain.property.dto.response.MyPropertyTableResponse;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.service.PropertyService;
import subscribers.clearbunyang.global.config.SecurityConfig;
import subscribers.clearbunyang.global.dto.PagedDto;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;
import subscribers.clearbunyang.security.AuthenticationFilterMocking;
import subscribers.clearbunyang.security.annotation.WithMockCustomAdmin;
import subscribers.clearbunyang.security.annotation.WithMockCustomMember;
import subscribers.clearbunyang.testfixtures.PropertySaveRequestDTOFixture;
import subscribers.clearbunyang.testfixtures.PropertyUpdateRequestDTOFixture;

@WebMvcTest(AdminPropertyController.class)
@Import({SecurityConfig.class})
@DisplayName("AdminPropertyController-단위 테스트")
public class AdminPropertyControllerTest extends AuthenticationFilterMocking {

    @Autowired private MockMvc mockMvc;
    @MockBean private PropertyService propertyService;
    @Autowired private ObjectMapper objectMapper;

    @DisplayName("물건 등록 테스트-admin일때")
    @Test
    @WithMockCustomAdmin
    public void addProperty1() throws Exception {
        PropertySaveRequest requestDTO = PropertySaveRequestDTOFixture.createDefault();
        Property mockProperty = new Property();
        when(propertyService.saveProperty(any(PropertySaveRequest.class), any(Long.class)))
                .thenReturn(mockProperty);

        mockMvc.perform(
                        post("/api/admin/properties")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(requestDTO))
                                .with(csrf()))
                .andExpect(status().isOk());
    }

    @DisplayName("물건 등록 테스트-member일때")
    @Test
    @WithMockCustomMember
    public void addProperty3() throws Exception {
        PropertySaveRequest requestDTO = PropertySaveRequestDTOFixture.createDefault();
        Property mockProperty = new Property();
        when(propertyService.saveProperty(any(PropertySaveRequest.class), any(Long.class)))
                .thenReturn(mockProperty);

        mockMvc.perform(
                        post("/api/admin/properties")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(requestDTO))
                                .with(csrf()))
                .andExpect(status().is4xxClientError());
    }

    @DisplayName("물건 등록 테스트: request validation 실패")
    @Test
    @WithMockCustomAdmin
    public void addProperty2() throws Exception {
        PropertySaveRequest requestDTO = PropertySaveRequestDTOFixture.createDefault();
        requestDTO.getAreas().add(new AreaRequest(60, 50000, 60000, 10));
        Property mockProperty = new Property();

        when(propertyService.saveProperty(any(PropertySaveRequest.class), any(Long.class)))
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

    @Test
    @DisplayName("등록한 매물 조회-첫번째 페이지네이션 테스트")
    @WithMockCustomAdmin
    public void getCardsByOffsetTest() throws Exception {
        List<MyPropertyCardResponse> cardResponseDTOList =
                List.of(
                        MyPropertyCardResponse.builder()
                                .id(1L)
                                .name("Test Property")
                                .addrDo("서울")
                                .addrGu("강남구")
                                .createdAt(LocalDateTime.now())
                                .endDate(LocalDate.now().plusDays(30))
                                .build());

        PagedDto<MyPropertyCardResponse> pagedDto =
                PagedDto.<MyPropertyCardResponse>builder()
                        .pageSize(4)
                        .totalPages(1)
                        .currentPage(0)
                        .contents(cardResponseDTOList)
                        .build();

        when(propertyService.getCards(anyInt(), anyInt(), anyLong())).thenReturn(pagedDto);

        mockMvc.perform(
                        get("/api/admin/my-properties/card")
                                .param("page", "0")
                                .param("size", "4")
                                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents[0].name").value("Test Property"))
                .andExpect(jsonPath("$.contents[0].addrDo").value("서울"))
                .andExpect(jsonPath("$.contents[0].addrGu").value("강남구"));
    }

    @Test
    @DisplayName("등록한 매물 조회-두번째 페이지네이션 테스트")
    @WithMockCustomAdmin
    public void getTablesByOffsetTest() throws Exception {
        List<MyPropertyTableResponse> tableResponseDTOList =
                List.of(
                        MyPropertyTableResponse.builder()
                                .id(1L)
                                .isPending(true)
                                .name("Test Property")
                                .totalCount(100)
                                .consultationPendingCount(5L)
                                .createdAt(LocalDate.now().minusDays(10))
                                .endDate(LocalDate.now().plusDays(20))
                                .build());

        PagedDto<MyPropertyTableResponse> pagedDto =
                PagedDto.<MyPropertyTableResponse>builder()
                        .pageSize(4)
                        .totalPages(1)
                        .currentPage(0)
                        .contents(tableResponseDTOList)
                        .build();

        when(propertyService.getTables(anyInt(), anyInt(), anyLong())).thenReturn(pagedDto);

        mockMvc.perform(
                        get("/api/admin/my-properties/table")
                                .param("page", "0")
                                .param("size", "4")
                                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents[0].name").value("Test Property"))
                .andExpect(jsonPath("$.contents[0].totalCount").value(100))
                .andExpect(jsonPath("$.contents[0].consultationPendingCount").value(5));
    }

    @Test
    @DisplayName("매물 수정 테스트")
    @WithMockCustomAdmin
    public void updateProperty1() throws Exception {
        when(propertyService.updateProperty(anyLong(), any(PropertyUpdateRequest.class), anyLong()))
                .thenReturn(new Property());
        PropertyUpdateRequest requestDTO = PropertyUpdateRequestDTOFixture.createDefault();

        mockMvc.perform(
                        patch("/api/admin/properties/{propertyId}", anyLong())
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(requestDTO))
                                .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("매물 수정 테스트- validation 발생")
    @WithMockCustomAdmin
    public void updateProperty2() throws Exception {
        when(propertyService.updateProperty(anyLong(), any(PropertyUpdateRequest.class), anyLong()))
                .thenReturn(new Property());
        PropertyUpdateRequest requestDTO = PropertyUpdateRequestDTOFixture.createDefault();
        requestDTO.setInfra(null);
        requestDTO.setBenefit(null);

        mockMvc.perform(
                        patch("/api/admin/properties/{propertyId}", anyLong())
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(requestDTO))
                                .with(csrf()))
                .andExpect(status().isBadRequest());
    }
}
