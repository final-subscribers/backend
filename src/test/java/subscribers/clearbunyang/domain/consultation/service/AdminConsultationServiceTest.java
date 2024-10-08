package subscribers.clearbunyang.domain.consultation.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subscribers.clearbunyang.domain.auth.entity.Admin;
import subscribers.clearbunyang.domain.consultation.dto.adminConsultation.request.ConsultRequest;
import subscribers.clearbunyang.domain.consultation.dto.adminConsultation.response.ConsultCompletedResponse;
import subscribers.clearbunyang.domain.consultation.dto.adminConsultation.response.ConsultPendingResponse;
import subscribers.clearbunyang.domain.consultation.dto.adminConsultation.response.ConsultantListResponse;
import subscribers.clearbunyang.domain.consultation.dto.adminConsultation.response.ConsultantResponse;
import subscribers.clearbunyang.domain.consultation.entity.AdminConsultation;
import subscribers.clearbunyang.domain.consultation.entity.MemberConsultation;
import subscribers.clearbunyang.domain.consultation.entity.enums.Medium;
import subscribers.clearbunyang.domain.consultation.entity.enums.Status;
import subscribers.clearbunyang.domain.consultation.entity.enums.Tier;
import subscribers.clearbunyang.domain.consultation.exception.ConsultantException;
import subscribers.clearbunyang.domain.consultation.repository.AdminConsultationRepository;
import subscribers.clearbunyang.domain.consultation.repository.MemberConsultationRepository;
import subscribers.clearbunyang.domain.likes.entity.Likes;
import subscribers.clearbunyang.domain.property.entity.Area;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.entity.enums.PropertyType;
import subscribers.clearbunyang.domain.property.entity.enums.SalesType;
import subscribers.clearbunyang.domain.property.repository.PropertyRepository;
import subscribers.clearbunyang.global.exception.EntityNotFoundException;
import subscribers.clearbunyang.global.exception.InvalidValueException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;
import subscribers.clearbunyang.global.file.entity.File;

@ExtendWith(MockitoExtension.class)
class AdminConsultationServiceTest {

    @InjectMocks AdminConsultationService adminConsultationService;

    @Mock AdminConsultationRepository adminConsultationRepository;

    @Mock MemberConsultationRepository memberConsultationRepository;

    @Mock PropertyRepository propertyRepository;

    private Property property;

    private MemberConsultation memberConsultation;

    private AdminConsultation adminConsultation;

    @BeforeEach
    public void setUp() {
        property =
                Property.builder()
                        .id(1L)
                        .name("propertyName")
                        .constructor("constructor")
                        .areaAddr("areaAddr")
                        .modelHouseAddr("modelHouseAddr")
                        .phoneNumber("010-1234-1234")
                        .contactChannel(null)
                        .homePage("https://")
                        .likeCount(5)
                        .startDate(LocalDate.now().minusYears(2))
                        .endDate(LocalDate.now().plusYears(1))
                        .propertyType(PropertyType.APARTMENT)
                        .salesType(SalesType.LEASE_SALE)
                        .totalNumber(500)
                        .companyName("companyName")
                        .admin(new Admin())
                        .likes(List.of(new Likes()))
                        .files(List.of(new File()))
                        .areas(List.of(new Area()))
                        .build();

        memberConsultation =
                MemberConsultation.builder()
                        .id(1L)
                        .status(Status.COMPLETED)
                        .memberMessage("memberMessage")
                        .createdAt(LocalDateTime.now())
                        .preferredAt(LocalDate.now().plusDays(1))
                        .modifiedAt(LocalDateTime.now())
                        .memberName("memberName")
                        .phoneNumber("011-1234-1234")
                        .medium(Medium.LMS)
                        .property(new Property())
                        .adminConsultation(adminConsultation)
                        .build();

        adminConsultation =
                AdminConsultation.builder()
                        .id(1L)
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .tier(Tier.A)
                        .consultMessage("consultMessage")
                        .consultant("consultant")
                        .completedAt(LocalDate.now())
                        .memberConsultation(memberConsultation)
                        .build();
    }

    @Test
    void 상담완료_모달_정상작동() {

        lenient()
                .when(adminConsultationRepository.getById(anyLong()))
                .thenReturn(adminConsultation);
        ConsultCompletedResponse response =
                adminConsultationService.getConsultCompletedResponse(1L);
        assertNotNull(response);

        verify(adminConsultationRepository).getById(anyLong());
    }

    @Test
    void 상담대기_모달_정상작동() {

        MemberConsultation memberConsultation1 = createMemberConsultation();
        AdminConsultation adminConsultation1 = createAdminConsultation(memberConsultation1);
        lenient()
                .when(memberConsultationRepository.getById(anyLong()))
                .thenReturn(memberConsultation1);

        lenient()
                .when(memberConsultationRepository.checkExtraConsultation(anyString()))
                .thenReturn(true);

        ConsultPendingResponse response = adminConsultationService.getConsultPendingResponse(1L);

        assertNotNull(response);
    }

    @Test
    void 상담사목록_정상작동() {
        lenient().when(propertyRepository.getIdById(anyLong())).thenReturn(property.getId());

        ConsultantListResponse response = adminConsultationService.getConsultants(1L);
        assertNotNull(response);

        verify(propertyRepository).getIdById(anyLong());
    }

    @Test
    void 상담사메세지변경_정상작동() {
        adminConsultation.getMemberConsultation().setStatus(Status.COMPLETED);
        lenient()
                .when(adminConsultationRepository.getById(anyLong()))
                .thenReturn(adminConsultation);
        ConsultCompletedResponse response =
                adminConsultationService.updateConsultMessage(adminConsultation.getId(), "message");

        assertNotNull(response);

        verify(adminConsultationRepository).getById(anyLong());
    }

    @Test
    void 상담글작성_정상작동() {
        MemberConsultation memberConsultation1 = createMemberConsultation();
        AdminConsultation adminConsultation1 = createAdminConsultation(memberConsultation1);
        ConsultRequest request = createAdminConsultRequest();

        when(memberConsultationRepository.getById(anyLong())).thenReturn(memberConsultation1);

        adminConsultationService.createAdminConsult(1L, request);
        verify(memberConsultationRepository).getById(anyLong());
    }

    @Test
    void 상담사변경_정상작동() {
        MemberConsultation memberConsultation1 = createMemberConsultationLMS(property);
        AdminConsultation adminConsultation1 =
                createAdminConsultationConsultantNullAndLMS(memberConsultation1);
        AdminConsultation adminConsultation2 = createAdminConsultation(memberConsultation1);
        adminConsultation2.setConsultant("a-10");

        lenient()
                .when(adminConsultationRepository.getById(anyLong()))
                .thenReturn(adminConsultation1);

        lenient()
                .when(adminConsultationRepository.findByPropertyId(any()))
                .thenReturn(List.of(adminConsultation2));

        ConsultantResponse response =
                adminConsultationService.registerConsultant(1L, adminConsultation2.getConsultant());

        assertNotNull(response);

        verify(adminConsultationRepository).findByPropertyId(any());
    }

    @Test
    void 상담사등록_상담사를_변경할수없습니다() {
        when(adminConsultationRepository.getById(anyLong())).thenReturn(adminConsultation);

        when(adminConsultationRepository.findByPropertyId(any()))
                .thenReturn(List.of(adminConsultation));
        ConsultantException exception =
                assertThrows(
                        ConsultantException.class,
                        () ->
                                adminConsultationService.registerConsultant(
                                        1L, adminConsultation.getConsultant()));

        assertEquals(ErrorCode.UNABLE_TO_CHANGE_CONSULTANT.getMessage(), exception.getMessage());
    }

    @Test
    void 상담사등록_BAD_REQUEST() {
        AdminConsultation adminConsultation1 = createAdminConsultationConsultantNull();

        when(adminConsultationRepository.getById(anyLong())).thenReturn(adminConsultation1);

        lenient()
                .when(adminConsultationRepository.findByPropertyId(any()))
                .thenReturn(List.of(adminConsultation1));

        InvalidValueException exception =
                assertThrows(
                        InvalidValueException.class,
                        () ->
                                adminConsultationService.registerConsultant(
                                        1L, adminConsultation1.getConsultant()));

        assertEquals(ErrorCode.BAD_REQUEST.getMessage(), exception.getMessage());
    }

    @Test
    void 상담글작성_memberConsultation_NOT_FOUND() {
        ConsultRequest request = createAdminConsultRequest();
        when(memberConsultationRepository.getById(any()))
                .thenThrow(new EntityNotFoundException(ErrorCode.NOT_FOUND));
        EntityNotFoundException exception =
                assertThrows(
                        EntityNotFoundException.class,
                        () -> adminConsultationService.createAdminConsult(anyLong(), request));

        assertTrue(exception.getMessage().contains(ErrorCode.NOT_FOUND.getMessage()));
    }

    @Test
    void 상담사메세지_변경_AdminConsultation_NOT_FOUND() {
        when(adminConsultationRepository.getById(anyLong()))
                .thenThrow(new EntityNotFoundException(ErrorCode.NOT_FOUND));

        EntityNotFoundException exception =
                assertThrows(
                        EntityNotFoundException.class,
                        () -> {
                            adminConsultationService.updateConsultMessage(anyLong(), "sdf");
                        });

        assertEquals(ErrorCode.NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void 상담사메세지_변경_상태가_PENDING() {
        MemberConsultation memberConsultation1 = createMemberConsultation();
        AdminConsultation adminConsultation1 = createAdminConsultation(memberConsultation1);
        when(adminConsultationRepository.getById(anyLong())).thenReturn(adminConsultation1);

        InvalidValueException exception =
                assertThrows(
                        InvalidValueException.class,
                        () -> {
                            adminConsultationService.updateConsultMessage(anyLong(), "fg");
                        });

        assertEquals(ErrorCode.NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 상담완료_모달_Admin_Consultation_NOT_FOUND() {
        when(adminConsultationRepository.getById(anyLong()))
                .thenThrow(new EntityNotFoundException(ErrorCode.NOT_FOUND));

        EntityNotFoundException exception =
                assertThrows(
                        EntityNotFoundException.class,
                        () -> {
                            adminConsultationService.getConsultCompletedResponse(anyLong());
                        });

        assertEquals(ErrorCode.NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void 상담완료모달_상태가_PENDING() {
        when(adminConsultationRepository.getById(anyLong()))
                .thenThrow(new EntityNotFoundException(ErrorCode.NOT_FOUND));

        EntityNotFoundException exception =
                assertThrows(
                        EntityNotFoundException.class,
                        () -> {
                            adminConsultationService.getConsultCompletedResponse(anyLong());
                        });

        assertEquals(ErrorCode.NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void 상담대기모달_상태가_COMPLETED() {
        when(memberConsultationRepository.getById(anyLong()))
                .thenThrow(new EntityNotFoundException(ErrorCode.NOT_FOUND));

        EntityNotFoundException exception =
                assertThrows(
                        EntityNotFoundException.class,
                        () -> {
                            adminConsultationService.getConsultPendingResponse(anyLong());
                        });

        assertEquals(ErrorCode.NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void 상담사변경_Admin_Consultation_NOT_FOUND() {
        when(adminConsultationRepository.getById(anyLong()))
                .thenThrow(new EntityNotFoundException(ErrorCode.NOT_FOUND));

        EntityNotFoundException exception =
                assertThrows(
                        EntityNotFoundException.class,
                        () -> {
                            adminConsultationService.registerConsultant(anyLong(), "consultant");
                        });

        assertEquals(ErrorCode.NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void 상담사변경_Property_NOT_FOUND() {
        lenient()
                .when(adminConsultationRepository.getById(anyLong()))
                .thenReturn(adminConsultation);

        when(adminConsultationRepository.getById(anyLong()))
                .thenThrow(new EntityNotFoundException(ErrorCode.NOT_FOUND));
        EntityNotFoundException exception =
                assertThrows(
                        EntityNotFoundException.class,
                        () -> {
                            adminConsultationService.registerConsultant(anyLong(), "consultant");
                        });

        assertEquals(ErrorCode.NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void 상담사변경_없는상담사선택() {
        lenient()
                .when(adminConsultationRepository.getById(anyLong()))
                .thenReturn(adminConsultation);

        when(adminConsultationRepository.findByPropertyId(any()))
                .thenThrow(new InvalidValueException(ErrorCode.NOT_FOUND));

        InvalidValueException exception =
                assertThrows(
                        InvalidValueException.class,
                        () -> {
                            adminConsultationService.registerConsultant(anyLong(), "kb");
                        });

        assertEquals(ErrorCode.NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void 상담사리스트_Property_NOT_FOUND() {
        when(propertyRepository.getIdById(anyLong()))
                .thenThrow(new EntityNotFoundException(ErrorCode.NOT_FOUND));

        EntityNotFoundException exception =
                assertThrows(
                        EntityNotFoundException.class,
                        () -> {
                            adminConsultationService.getConsultants(anyLong());
                        });

        assertEquals(ErrorCode.NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void 상담대기모달_Admin_Consultation_NOT_FOUND() {
        when(memberConsultationRepository.getById(anyLong()))
                .thenThrow(new EntityNotFoundException(ErrorCode.NOT_FOUND));

        EntityNotFoundException exception =
                assertThrows(
                        EntityNotFoundException.class,
                        () -> {
                            adminConsultationService.getConsultPendingResponse(anyLong());
                        });

        assertEquals(ErrorCode.NOT_FOUND.getMessage(), exception.getMessage());
    }

    private AdminConsultation createAdminConsultation(MemberConsultation memberConsultation1) {
        return AdminConsultation.builder()
                .id(1L)
                .tier(Tier.A)
                .consultMessage("consultMessage")
                .createdAt(LocalDateTime.now())
                .consultant("consultant")
                .memberConsultation(memberConsultation1)
                .build();
    }

    private AdminConsultation createAdminConsultation() {
        return AdminConsultation.builder()
                .id(2L)
                .tier(Tier.A)
                .consultMessage("consultMessage")
                .createdAt(LocalDateTime.now())
                .consultant("a-10")
                .memberConsultation(new MemberConsultation())
                .build();
    }

    private AdminConsultation createAdminConsultationConsultantNull() {
        return AdminConsultation.builder()
                .id(1L)
                .tier(Tier.A)
                .consultMessage("consultMessage")
                .createdAt(LocalDateTime.now())
                .consultant(null)
                .memberConsultation(new MemberConsultation())
                .build();
    }

    private AdminConsultation createAdminConsultationConsultantNullAndLMS(
            MemberConsultation memberConsultation) {
        return AdminConsultation.builder()
                .id(1L)
                .tier(Tier.A)
                .consultMessage("consultMessage")
                .createdAt(LocalDateTime.now())
                .consultant(null)
                .memberConsultation(memberConsultation)
                .build();
    }

    private MemberConsultation createMemberConsultationLMS(Property property) {
        return MemberConsultation.builder()
                .id(1L)
                .status(Status.PENDING)
                .memberMessage("memberMessage")
                .createdAt(LocalDateTime.now())
                .preferredAt(LocalDate.now().plusDays(1))
                .modifiedAt(LocalDateTime.now())
                .memberName("memberName")
                .phoneNumber("011-1234-1234")
                .medium(Medium.LMS)
                .property(property)
                .adminConsultation(new AdminConsultation())
                .build();
    }

    private MemberConsultation createMemberConsultation() {
        return MemberConsultation.builder()
                .id(1L)
                .status(Status.PENDING)
                .memberMessage("memberMessage")
                .createdAt(LocalDateTime.now())
                .preferredAt(LocalDate.now().plusDays(1))
                .modifiedAt(LocalDateTime.now())
                .memberName("memberName")
                .phoneNumber("011-1234-1234")
                .medium(Medium.PHONE)
                .property(new Property())
                .adminConsultation(new AdminConsultation())
                .build();
    }

    private MemberConsultation createMemberConsultation(AdminConsultation adminConsultation) {
        return MemberConsultation.builder()
                .id(1L)
                .status(Status.COMPLETED)
                .memberMessage("memberMessage")
                .createdAt(LocalDateTime.now())
                .preferredAt(LocalDate.now().plusDays(1))
                .modifiedAt(LocalDateTime.now())
                .memberName("memberName")
                .phoneNumber("011-1234-1234")
                .adminConsultation(adminConsultation)
                .medium(Medium.LMS)
                .property(property)
                .build();
    }

    private ConsultRequest createAdminConsultRequest() {
        return ConsultRequest.builder()
                .status(Status.PENDING)
                .tier(Tier.A)
                .consultantMessage("consultantMessage")
                .build();
    }

    private Property createProperty() {
        return Property.builder()
                .id(1L)
                .name("propertyName")
                .constructor("constructor")
                .areaAddr("areaAddr")
                .modelHouseAddr("modelHouseAddr")
                .phoneNumber("010-1234-1234")
                .contactChannel(null)
                .homePage("https://")
                .likeCount(5)
                .startDate(LocalDate.now().minusYears(2))
                .endDate(LocalDate.now().plusYears(1))
                .propertyType(PropertyType.APARTMENT)
                .salesType(SalesType.LEASE_SALE)
                .totalNumber(500)
                .companyName("companyName")
                .admin(new Admin())
                .likes(List.of(new Likes()))
                .files(List.of(new File()))
                .areas(List.of(new Area()))
                .build();
    }
}
