package subscribers.clearbunyang.domain.consultation.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subscribers.clearbunyang.domain.consultation.entity.AdminConsultation;
import subscribers.clearbunyang.domain.consultation.entity.MemberConsultation;
import subscribers.clearbunyang.domain.consultation.entity.enums.Medium;
import subscribers.clearbunyang.domain.consultation.entity.enums.Status;
import subscribers.clearbunyang.domain.consultation.entity.enums.Tier;
import subscribers.clearbunyang.domain.consultation.exception.ConsultationException;
import subscribers.clearbunyang.domain.consultation.model.request.ConsultRequest;
import subscribers.clearbunyang.domain.consultation.model.response.ConsultCompletedResponse;
import subscribers.clearbunyang.domain.consultation.model.response.ConsultPendingResponse;
import subscribers.clearbunyang.domain.consultation.model.response.ConsultantListResponse;
import subscribers.clearbunyang.domain.consultation.model.response.ConsultantResponse;
import subscribers.clearbunyang.domain.consultation.repository.AdminConsultationRepository;
import subscribers.clearbunyang.domain.consultation.repository.MemberConsultationRepository;
import subscribers.clearbunyang.domain.file.entity.File;
import subscribers.clearbunyang.domain.like.entity.Likes;
import subscribers.clearbunyang.domain.property.entity.Area;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.entity.enums.PropertyType;
import subscribers.clearbunyang.domain.property.entity.enums.SalesType;
import subscribers.clearbunyang.domain.property.repository.PropertyRepository;
import subscribers.clearbunyang.domain.user.entity.Admin;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;
import subscribers.clearbunyang.global.exception.notFound.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class ConsultationServiceTest {

    @InjectMocks
    ConsultationService consultationService;

    @Mock
    AdminConsultationRepository adminConsultationRepository;

    @Mock
    MemberConsultationRepository memberConsultationRepository;

    @Mock
    PropertyRepository propertyRepository;

    private Property property;

    private MemberConsultation memberConsultation;

    private AdminConsultation adminConsultation;

    @BeforeEach
    public void setUp() {
        property = Property.builder()
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
                .likes(Set.of(new Likes()))
                .files(List.of(new File()))
                .areas(List.of(new Area()))
                .build();

        memberConsultation = MemberConsultation.builder()
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

        adminConsultation = AdminConsultation.builder()
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

        lenient().when(adminConsultationRepository.getById(anyLong()))
                .thenReturn(adminConsultation);
        ConsultCompletedResponse response = consultationService.getConsultCompletedResponse(
                1L);
        assertNotNull(response);

        verify(adminConsultationRepository).getById(anyLong());
    }

    @Test
    void 상담대기_모달_정상작동() {

        MemberConsultation memberConsultation1 = createMemberConsultation();
        AdminConsultation adminConsultation1 = createAdminConsultation(memberConsultation1);
        lenient().when(memberConsultationRepository.getById(anyLong()))
                .thenReturn(memberConsultation1);

        lenient().when(memberConsultationRepository.checkExtraConsultation(anyString()))
                .thenReturn(true);

        ConsultPendingResponse response = consultationService.getConsultPendingResponse(1L);

        assertNotNull(response);
    }

    @Test
    void 상담사목록_정상작동() {
        lenient().when(propertyRepository.getIdById(anyLong())).thenReturn(property.getId());
        lenient().when(adminConsultationRepository.findAllConsultantByPropertyId(any()))
                .thenReturn(List.of(adminConsultation));

        ConsultantListResponse response = consultationService.getConsultants(1L);
        assertNotNull(response);

        verify(propertyRepository).getIdById(anyLong());
    }

    @Test
    void 상담사메세지변경_정상작동() {
        adminConsultation.getMemberConsultation().setStatus(Status.COMPLETED);
        lenient().when(adminConsultationRepository.getById(anyLong()))
                .thenReturn(adminConsultation);
        ConsultCompletedResponse response = consultationService.updateConsultMessage(
                adminConsultation.getId(), "message");

        assertNotNull(response);

        verify(adminConsultationRepository).getById(anyLong());
    }

    @Test
    void 상담글작성_정상작동() {
        MemberConsultation memberConsultation1 = createMemberConsultation();
        AdminConsultation adminConsultation1 = createAdminConsultation(memberConsultation1);
        ConsultRequest request = createAdminConsultRequest();

        when(memberConsultationRepository.getById(anyLong())).thenReturn(memberConsultation1);

        consultationService.createAdminConsult(1L, request);
        verify(memberConsultationRepository).getById(anyLong());
    }

    @Test
    void 상담사변경_정상작동() {
        lenient().when(adminConsultationRepository.getById(anyLong()))
                .thenReturn(adminConsultation);

        lenient().when(adminConsultationRepository.findByPropertyId(any()))
                .thenReturn(List.of(adminConsultation));

        ConsultantResponse response = consultationService.changeConsultant(1L,
                adminConsultation.getConsultant());

        assertNotNull(response);

        verify(adminConsultationRepository).findByPropertyId(any());
    }

    @Test
    void 상담글작성_memberConsultation_NOT_FOUND() {
        ConsultRequest request = createAdminConsultRequest();
        when(memberConsultationRepository.getById(any())).thenThrow(
                new EntityNotFoundException(ErrorCode.NOT_FOUND)
        );
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                consultationService.createAdminConsult(anyLong(), request));

        assertTrue(exception.getMessage().contains(ErrorCode.NOT_FOUND.getMessage()));
    }

    @Test
    void 상담사메세지_변경_AdminConsultation_NOT_FOUND() {
        when(adminConsultationRepository.getById(anyLong())).thenThrow(
                new EntityNotFoundException(ErrorCode.NOT_FOUND));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            consultationService.updateConsultMessage(anyLong(), "sdf");
        });

        assertEquals(ErrorCode.NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void 상담사메세지_변경_상태가_PENDING() {
        MemberConsultation memberConsultation1 = createMemberConsultation();
        AdminConsultation adminConsultation1 = createAdminConsultation(memberConsultation1);
        when(adminConsultationRepository.getById(anyLong())).thenReturn(adminConsultation1);

        ConsultationException exception = assertThrows(ConsultationException.class, () -> {
            consultationService.updateConsultMessage(anyLong(), "fg");
        });

        assertEquals(ErrorCode.NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 상담완료_모달_Admin_Consultation_NOT_FOUND() {
        when(adminConsultationRepository.getById(anyLong())).thenThrow(
                new EntityNotFoundException(ErrorCode.NOT_FOUND));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            consultationService.getConsultCompletedResponse(anyLong());
        });

        assertEquals(ErrorCode.NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void 상담완료모달_상태가_PENDING() {
        when(adminConsultationRepository.getById(anyLong())).thenThrow(
                new EntityNotFoundException(ErrorCode.NOT_FOUND));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            consultationService.getConsultCompletedResponse(anyLong());
        });

        assertEquals(ErrorCode.NOT_FOUND.getMessage(), exception.getMessage());

    }

    @Test
    void 상담대기모달_상태가_COMPLETED() {
        when(memberConsultationRepository.getById(anyLong())).thenThrow(
                new EntityNotFoundException(ErrorCode.NOT_FOUND));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            consultationService.getConsultPendingResponse(anyLong());
        });

        assertEquals(ErrorCode.NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void 상담사변경_Admin_Consultation_NOT_FOUND() {
        when(adminConsultationRepository.getById(anyLong())).thenThrow(
                new EntityNotFoundException(ErrorCode.NOT_FOUND));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            consultationService.changeConsultant(anyLong(), "consultant");
        });

        assertEquals(ErrorCode.NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void 상담사변경_Property_NOT_FOUND() {
        lenient().when(adminConsultationRepository.getById(anyLong()))
                .thenReturn(adminConsultation);

        when(adminConsultationRepository.getById(anyLong())).thenThrow(
                new EntityNotFoundException(ErrorCode.NOT_FOUND))
        ;
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            consultationService.changeConsultant(anyLong(), "consultant");
        });

        assertEquals(ErrorCode.NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void 상담사변경_없는상담사선택() {
        lenient().when(adminConsultationRepository.getById(anyLong()))
                .thenReturn(adminConsultation);

        when(adminConsultationRepository.findByPropertyId(any()))
                .thenThrow(new ConsultationException(ErrorCode.NOT_FOUND));

        ConsultationException exception = assertThrows(ConsultationException.class, () -> {
            consultationService.changeConsultant(anyLong(), "kb");
        });

        assertEquals(ErrorCode.NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void 상담사리스트_Property_NOT_FOUND() {
        when(propertyRepository.getIdById(anyLong())).thenThrow(
                new EntityNotFoundException(ErrorCode.NOT_FOUND));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            consultationService.getConsultants(anyLong());
        });

        assertEquals(ErrorCode.NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void 상담사리스트_없는상담사선택() {
        when(adminConsultationRepository.findAllConsultantByPropertyId(anyLong())).thenThrow(
                new ConsultationException(ErrorCode.NOT_FOUND));

        ConsultationException exception = assertThrows(ConsultationException.class, () -> {
            consultationService.getConsultants(anyLong());
        });

        assertEquals(ErrorCode.NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void 상담대기모달_Admin_Consultation_NOT_FOUND() {
        when(memberConsultationRepository.getById(anyLong())).thenThrow(
                new EntityNotFoundException(ErrorCode.NOT_FOUND));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            consultationService.getConsultPendingResponse(anyLong());
        });

        assertEquals(ErrorCode.NOT_FOUND.getMessage(), exception.getMessage());
    }

    private AdminConsultation createAdminConsultation(MemberConsultation memberConsultation1) {
        return AdminConsultation.builder()
                .id(2L)
                .tier(Tier.A)
                .consultMessage("consultMessage")
                .createdAt(LocalDateTime.now())
                .consultant("consultant")
                .memberConsultation(memberConsultation1)
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
                .medium(Medium.LMS)
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
                .likes(Set.of(new Likes()))
                .files(List.of(new File()))
                .areas(List.of(new Area()))
                .build();
    }
}