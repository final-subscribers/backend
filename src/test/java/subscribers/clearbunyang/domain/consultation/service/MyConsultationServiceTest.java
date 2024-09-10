package subscribers.clearbunyang.domain.consultation.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subscribers.clearbunyang.domain.consultation.entity.MemberConsultation;
import subscribers.clearbunyang.domain.consultation.entity.enums.Medium;
import subscribers.clearbunyang.domain.consultation.entity.enums.Status;
import subscribers.clearbunyang.domain.consultation.model.myConsultations.ConsultationPagedResponse;
import subscribers.clearbunyang.domain.consultation.repository.MemberConsultationRepository;
import subscribers.clearbunyang.domain.file.entity.File;
import subscribers.clearbunyang.domain.like.entity.Likes;
import subscribers.clearbunyang.domain.property.entity.Area;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.entity.enums.PropertyType;
import subscribers.clearbunyang.domain.property.entity.enums.SalesType;
import subscribers.clearbunyang.domain.user.entity.Admin;
import subscribers.clearbunyang.domain.user.entity.Member;
import subscribers.clearbunyang.domain.user.entity.enums.UserRole;
import subscribers.clearbunyang.domain.user.repository.MemberRepository;
import subscribers.clearbunyang.global.exception.Invalid.InvalidValueException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;

@ExtendWith(MockitoExtension.class)
public class MyConsultationServiceTest {

    @InjectMocks MyConsultationService myConsultationService;

    @Mock MemberConsultationRepository memberConsultationRepository;

    @Mock MemberRepository memberRepository;

    private Property property1;

    private Property property2;

    private MemberConsultation memberPendingConsultation1;

    private MemberConsultation memberPendingConsultation2;

    private MemberConsultation memberCompletedConsultation1;

    private MemberConsultation memberCompletedConsultation2;

    private Member member;

    @BeforeEach
    public void setUp() throws Exception {
        member =
                Member.builder()
                        .id(1L)
                        .name("test")
                        .email("test@gmail.com")
                        .password("abcd")
                        .phoneNumber("01012345678")
                        .address("address")
                        .role(UserRole.MEMBER)
                        .build();

        property1 =
                Property.builder()
                        .id(1L)
                        .name("propertyName1")
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

        property2 =
                Property.builder()
                        .id(2L)
                        .name("propertyName2")
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

        memberPendingConsultation1 =
                MemberConsultation.builder()
                        .id(1L)
                        .status(Status.PENDING)
                        .memberMessage("memberMessage")
                        .createdAt(LocalDateTime.now())
                        .preferredAt(LocalDate.now().plusDays(1))
                        .modifiedAt(LocalDateTime.now())
                        .memberName("test")
                        .phoneNumber("01012345678")
                        .medium(Medium.LMS)
                        .property(property1)
                        .build();

        memberPendingConsultation2 =
                MemberConsultation.builder()
                        .id(2L)
                        .status(Status.PENDING)
                        .memberMessage("memberMessage")
                        .createdAt(LocalDateTime.now())
                        .preferredAt(LocalDate.now().plusDays(1))
                        .modifiedAt(LocalDateTime.now())
                        .memberName("test")
                        .phoneNumber("01012345678")
                        .medium(Medium.LMS)
                        .property(property2)
                        .build();

        memberCompletedConsultation1 =
                MemberConsultation.builder()
                        .id(3L)
                        .status(Status.COMPLETED)
                        .memberMessage("memberMessage")
                        .createdAt(LocalDateTime.now())
                        .preferredAt(LocalDate.now().plusDays(1))
                        .modifiedAt(LocalDateTime.now())
                        .memberName("test")
                        .phoneNumber("01012345678")
                        .medium(Medium.LMS)
                        .property(property1)
                        .build();

        memberCompletedConsultation2 =
                MemberConsultation.builder()
                        .id(4L)
                        .status(Status.COMPLETED)
                        .memberMessage("memberMessage")
                        .createdAt(LocalDateTime.now())
                        .preferredAt(LocalDate.now().plusDays(1))
                        .modifiedAt(LocalDateTime.now())
                        .memberName("test")
                        .phoneNumber("01012345678")
                        .medium(Medium.LMS)
                        .property(property2)
                        .build();
    }

    @Test
    void getMyPendingConsultationsListSearchIsNullShouldSuccess() {
        Long userId = 1L;
        String search = "";
        int page = 0;
        int size = 5;

        when(memberRepository.findById(userId)).thenReturn(Optional.of(member));
        when(memberConsultationRepository.findPendingConsultationsByUserIdAndSearch(userId, search))
                .thenReturn(List.of(memberPendingConsultation1, memberPendingConsultation1));
        when(memberConsultationRepository.countConsultationsByUserId(userId)).thenReturn(4);

        ConsultationPagedResponse response =
                myConsultationService.getMyPendingConsultationsList(userId, search, page, size);

        assertNotNull(response);
        assertEquals(4, response.getTotalCount());
        assertEquals(1, response.getPagedData().getTotalPages());
        assertEquals(5, response.getPagedData().getPageSize());
        assertEquals(0, response.getPagedData().getCurrentPage());
        assertFalse(response.getPagedData().getContent().isEmpty());
        assertEquals(2, response.getPagedData().getContent().size());

        verify(memberRepository, times(1)).findById(userId);
        verify(memberConsultationRepository, times(1))
                .findPendingConsultationsByUserIdAndSearch(userId, search);
        verify(memberConsultationRepository, times(1)).countConsultationsByUserId(userId);
    }

    @Test
    void getMyPendingConsultationsListSearchIsNotNullShouldSuccess() {
        Long userId = 1L;
        String search = "propertyName2";
        int page = 0;
        int size = 5;

        when(memberRepository.findById(userId)).thenReturn(Optional.of(member));
        when(memberConsultationRepository.findPendingConsultationsByUserIdAndSearch(userId, search))
                .thenReturn(List.of(memberPendingConsultation1));
        when(memberConsultationRepository.countConsultationsByUserId(userId)).thenReturn(4);

        ConsultationPagedResponse response =
                myConsultationService.getMyPendingConsultationsList(userId, search, page, size);

        assertNotNull(response);
        assertEquals(4, response.getTotalCount());
        assertEquals(1, response.getPagedData().getTotalPages());
        assertEquals(5, response.getPagedData().getPageSize());
        assertEquals(0, response.getPagedData().getCurrentPage());
        assertFalse(response.getPagedData().getContent().isEmpty());
        assertEquals(1, response.getPagedData().getContent().size());

        verify(memberRepository, times(1)).findById(userId);
        verify(memberConsultationRepository, times(1))
                .findPendingConsultationsByUserIdAndSearch(userId, search);
        verify(memberConsultationRepository, times(1)).countConsultationsByUserId(userId);
    }

    @Test
    void getMyPendingConsultationsListUserNotFoundShouldThrowException() {
        Long userId = 999L;
        String search = "";
        int page = 0;
        int size = 5;

        when(memberRepository.findById(userId)).thenReturn(Optional.empty());

        InvalidValueException exception =
                assertThrows(
                        InvalidValueException.class,
                        () ->
                                myConsultationService.getMyPendingConsultationsList(
                                        userId, search, page, size));

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());

        verify(memberRepository, times(1)).findById(userId);
        verify(memberConsultationRepository, times(0))
                .findPendingConsultationsByUserIdAndSearch(anyLong(), anyString());
    }

    @Test
    void getMyCompletedConsultationsListSearchIsNullShouldSuccess() {
        Long userId = 1L;
        String search = "";
        int page = 0;
        int size = 5;

        when(memberRepository.findById(userId)).thenReturn(Optional.of(member));
        when(memberConsultationRepository.findCompletedConsultationsByUserIdAndSearch(
                        userId, search))
                .thenReturn(List.of(memberCompletedConsultation1, memberCompletedConsultation1));
        when(memberConsultationRepository.countConsultationsByUserId(userId)).thenReturn(4);

        ConsultationPagedResponse response =
                myConsultationService.getMyCompletedConsultationsList(userId, search, page, size);

        assertNotNull(response);
        assertEquals(4, response.getTotalCount());
        assertEquals(1, response.getPagedData().getTotalPages());
        assertEquals(5, response.getPagedData().getPageSize());
        assertEquals(0, response.getPagedData().getCurrentPage());
        assertFalse(response.getPagedData().getContent().isEmpty());
        assertEquals(2, response.getPagedData().getContent().size());

        verify(memberRepository, times(1)).findById(userId);
        verify(memberConsultationRepository, times(1))
                .findCompletedConsultationsByUserIdAndSearch(userId, search);
        verify(memberConsultationRepository, times(1)).countConsultationsByUserId(userId);
    }

    @Test
    void getMyCompletedConsultationsListSearchIsNotNullShouldSuccess() {
        Long userId = 1L;
        String search = "propertyName2";
        int page = 0;
        int size = 5;

        when(memberRepository.findById(userId)).thenReturn(Optional.of(member));
        when(memberConsultationRepository.findCompletedConsultationsByUserIdAndSearch(
                        userId, search))
                .thenReturn(List.of(memberCompletedConsultation1));
        when(memberConsultationRepository.countConsultationsByUserId(userId)).thenReturn(4);

        ConsultationPagedResponse response =
                myConsultationService.getMyCompletedConsultationsList(userId, search, page, size);

        assertNotNull(response);
        assertEquals(4, response.getTotalCount());
        assertEquals(1, response.getPagedData().getTotalPages());
        assertEquals(5, response.getPagedData().getPageSize());
        assertEquals(0, response.getPagedData().getCurrentPage());
        assertFalse(response.getPagedData().getContent().isEmpty());
        assertEquals(1, response.getPagedData().getContent().size());

        verify(memberRepository, times(1)).findById(userId);
        verify(memberConsultationRepository, times(1))
                .findCompletedConsultationsByUserIdAndSearch(userId, search);
        verify(memberConsultationRepository, times(1)).countConsultationsByUserId(userId);
    }

    @Test
    void getMyCompletedConsultationsListUserNotFoundShouldThrowException() {
        Long userId = 999L;
        String search = "";
        int page = 0;
        int size = 5;

        when(memberRepository.findById(userId)).thenReturn(Optional.empty());

        InvalidValueException exception =
                assertThrows(
                        InvalidValueException.class,
                        () ->
                                myConsultationService.getMyPendingConsultationsList(
                                        userId, search, page, size));

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());

        verify(memberRepository, times(1)).findById(userId);
        verify(memberConsultationRepository, times(0))
                .findPendingConsultationsByUserIdAndSearch(anyLong(), anyString());
    }
}
