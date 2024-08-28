package subscribers.clearbunyang.domain.consultation.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.mockito.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import subscribers.clearbunyang.domain.consultation.entity.AdminConsultation;
import subscribers.clearbunyang.domain.consultation.entity.MemberConsultation;
import subscribers.clearbunyang.domain.consultation.entity.enums.Medium;
import subscribers.clearbunyang.domain.consultation.entity.enums.Status;
import subscribers.clearbunyang.domain.consultation.entity.enums.Tier;
import subscribers.clearbunyang.domain.consultation.exception.ConsultationException;
import subscribers.clearbunyang.domain.consultation.model.request.ConsultRequest;
import subscribers.clearbunyang.domain.consultation.model.request.NewCustomerAdditionRequest;
import subscribers.clearbunyang.domain.consultation.model.response.ConsultCompletedListResponse;
import subscribers.clearbunyang.domain.consultation.model.response.ConsultPendingListResponse;
import subscribers.clearbunyang.domain.consultation.model.response.PagedDTO;
import subscribers.clearbunyang.domain.consultation.model.response.SideBarListResponse;
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
class PropertiesServiceTest {

    @InjectMocks
    PropertiesService propertiesService;

    @Mock
    AdminConsultationRepository adminConsultationRepository;

    @Mock
    MemberConsultationRepository memberConsultationRepository;

    @Mock
    PropertyRepository propertyRepository;

    private Property property;

    private MemberConsultation memberConsultation;

    private AdminConsultation adminConsultation;

    private SideBarListResponse sideBarListResponse;

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
                .status(Status.PENDING)
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
                .consultMessage("cosultMessage")
                .consultant("consultant")
                .completedAt(LocalDate.now())
                .memberConsultation(memberConsultation)
                .build();
    }

    @Test
    void 신규고객등록시_정상적동() {
        lenient().when(memberConsultationRepository.existsByPhoneNumber(anyString()))
                .thenReturn(true);
        lenient().when(propertyRepository.getById(anyLong())).thenReturn(new Property());

        lenient().when(adminConsultationRepository.save(any(AdminConsultation.class)))
                .thenReturn(new AdminConsultation());
        lenient().when(memberConsultationRepository.save(any(MemberConsultation.class)))
                .thenReturn(new MemberConsultation());

        MemberConsultation memberConsultation = new MemberConsultation();
        memberConsultationRepository.save(memberConsultation);

        AdminConsultation adminConsultation = new AdminConsultation();
        adminConsultationRepository.save(adminConsultation);

        //결과 검증
        assertNotNull(adminConsultation);

        //mock 객체에서 메서드 호출 검즡
        verify(memberConsultationRepository).save(any(MemberConsultation.class));
        verify(adminConsultationRepository).save(any(AdminConsultation.class));
    }

    @Test
    void 사이드바_정상작동() {
        Pageable pageable = PageRequest.of(0, 20);
        lenient().when(propertyRepository.getPendingProperties(LocalDate.now(), pageable))
                .thenReturn(
                        List.of(property));
        lenient().when(propertyRepository.getCompletedProperties(LocalDate.now(), pageable))
                .thenReturn(List.of(property));
        lenient().when(propertyRepository.getById(anyLong())).thenReturn(property);

        sideBarListResponse = propertiesService.getSideBarList(property.getId());

        assertNotNull(sideBarListResponse);

        verify(propertyRepository).getById(anyLong());
    }

    @Test
    void 상담대기리스트_정상작동() {
        MemberConsultation memberConsultation1 = createNewCustomerAddition();
        AdminConsultation adminConsultation1 = createAdminConsultation(memberConsultation1);

        Page<AdminConsultation> page = new PageImpl<>(List.of(adminConsultation1),
                PageRequest.of(0, 1), List.of(adminConsultation1).size());

        lenient().when(propertyRepository.getById(anyLong())).thenReturn(property);
        lenient().when(adminConsultationRepository.findByPropertyIdAndPendingAndFilters(
                anyLong(),
                any(Status.class),
                anyString(),
                anyString(),
                any(LocalDate.class),
                anyString(),
                any(Pageable.class))
        ).thenReturn(page);

        lenient().when(memberConsultationRepository.checkExtraConsultation(anyString()))
                .thenReturn(true);

        PagedDTO<ConsultPendingListResponse> pagedDTO = propertiesService.getConsultPendingListResponse(
                property.getId(), "name 010", "a", LocalDate.now().plusDays(1), 0, 1);

        assertNotNull(pagedDTO);
        ConsultPendingListResponse consultPendingListResponse = pagedDTO.getContent();
        assertNotNull(consultPendingListResponse);
        verify(propertyRepository).getById(anyLong());
    }

    @Test
    void 상담완료리스트_정상작동() {
        Page<AdminConsultation> page = new PageImpl<>(List.of(adminConsultation),
                PageRequest.of(0, 1), List.of(adminConsultation).size());

        lenient().when(propertyRepository.getById(anyLong())).thenReturn(property);
        lenient().when(adminConsultationRepository.findByPropertyIdAndCompletedAndFilters(
                anyLong(),
                any(Status.class),
                anyString(),
                anyString(),
                any(Tier.class),
                anyString(),
                any(LocalDate.class),
                any(Pageable.class))
        ).thenReturn(page);

        PagedDTO<ConsultCompletedListResponse> pagedDTO = propertiesService.getConsultCompletedListResponse(
                property.getId(), "name 010", Tier.A, "a", LocalDate.now().plusDays(1), 0, 1);

        assertNotNull(pagedDTO);
        ConsultCompletedListResponse counselCompletedListResponse = pagedDTO.getContent();
        assertNotNull(counselCompletedListResponse);

        verify(propertyRepository).getById(anyLong());
    }

    @Test
    void 신규고객등록시_전화번호_중복() {
        String number = memberConsultation.getPhoneNumber();
        doThrow(new ConsultationException(ErrorCode.PHONE_NUMBER_DUPLICATION))
                .when(memberConsultationRepository).checkPhoneNumberExists(number);

        NewCustomerAdditionRequest request = createNewCustomerAdditionRequest();

        ConsultationException exception = assertThrows(ConsultationException.class, () -> {
            propertiesService.createNewCustomerAddition(anyLong(), request);
        });

        assertEquals(ErrorCode.PHONE_NUMBER_DUPLICATION.getMessage(), exception.getMessage());
    }

    @Test
    void 신규고객등록시_해당매물_Property_NOT_FOUND() {
        when(propertyRepository.getById(anyLong())).thenThrow(
                new EntityNotFoundException(ErrorCode.NOT_FOUND));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            propertiesService.createNewCustomerAddition(anyLong(),
                    createNewCustomerAdditionRequest());
        });

        assertEquals(ErrorCode.NOT_FOUND.getMessage(), exception.getMessage());
    }


    @Test
    void 상담대기리스트_해당매물_Property_NOT_FOUND() {
        when(propertyRepository.getById(anyLong())).thenThrow(
                new EntityNotFoundException(ErrorCode.NOT_FOUND));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            propertiesService.getConsultPendingListResponse(property.getId(), "dfd", "fd",
                    LocalDate.now(), 1, 2);
        });
        assertEquals(ErrorCode.NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void 상담완료리스트_해당매물_Property_NOT_FOUND() {
        when(propertyRepository.getById(anyLong())).thenThrow(
                new EntityNotFoundException(ErrorCode.NOT_FOUND));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            propertiesService.getConsultCompletedListResponse(property.getId(), "name 010", Tier.A,
                    "a", LocalDate.now().plusDays(1), 0, 1);
        });

        assertEquals(ErrorCode.NOT_FOUND.getMessage(), exception.getMessage());
    }


    private NewCustomerAdditionRequest createNewCustomerAdditionRequest() {
        return NewCustomerAdditionRequest.builder()
                .name("name")
                .phoneNumber("011-1234-1234")
                .consultant("a-10")
                .preferredAt(LocalDate.now().plusDays(2))
                .consultingMessage("message")
                .tier(null)
                .status(Status.COMPLETED)
                .medium(Medium.LMS)
                .build();
    }

    private AdminConsultation createAdminConsultation() {
        return AdminConsultation.builder()
                .id(2L)
                .tier(Tier.A)
                .consultMessage("consultMessage")
                .createdAt(LocalDateTime.now())
                .consultant("consultant")
                .memberConsultation(memberConsultation)
                .build();
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

    private MemberConsultation createNewCustomerAddition() {
        return MemberConsultation.builder()
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
                .adminConsultation(new AdminConsultation())
                .build();
    }

    private MemberConsultation createNewCustomerAddition(AdminConsultation adminConsultation) {
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