package subscribers.clearbunyang.domain.like.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import subscribers.clearbunyang.domain.like.entity.Likes;
import subscribers.clearbunyang.domain.likes.repository.LikesRepository;
import subscribers.clearbunyang.domain.likes.service.LikesService;
import subscribers.clearbunyang.domain.property.repository.PropertyRepository;
import subscribers.clearbunyang.domain.user.entity.Member;
import subscribers.clearbunyang.domain.user.repository.MemberRepository;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;
import subscribers.clearbunyang.global.exception.notFound.EntityNotFoundException;

class LikesServiceTest {

    @Mock private LikesRepository likesRepository;

    @Mock private MemberRepository memberRepository;

    @Mock private PropertyRepository propertyRepository;

    @InjectMocks private LikesService likesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testToggleLike_MemberNotFound() {
        // 멤버 찾을 수 없을 때 에러 반환
        Long memberId = 1L;
        Long propertyId = 1L;

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(
                        EntityNotFoundException.class,
                        () -> likesService.toggleLike(memberId, propertyId));

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
        verify(likesRepository, never()).save(any(Likes.class));
        verify(likesRepository, never()).delete(any(Likes.class));
    }

    @Test
    void testToggleLike_PropertyNotFound() {
        // 물건 찾을 수 없을 때 에러 반환
        Long memberId = 1L;
        Long propertyId = 1L;
        Member member = new Member();

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(propertyRepository.findById(propertyId)).thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(
                        EntityNotFoundException.class,
                        () -> likesService.toggleLike(memberId, propertyId));

        assertEquals(ErrorCode.PROPERTY_NOT_FOUND, exception.getErrorCode());
        verify(likesRepository, never()).save(any(Likes.class));
        verify(likesRepository, never()).delete(any(Likes.class));
    }

    @Test
    void testGetMyFavoriteProperties_MemberNotFound() {
        // 멤버 찾을 수 없을 때 에러 반환
        Long memberId = 1L;
        String status = "open";
        int page = 0;
        int size = 10;

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(
                        EntityNotFoundException.class,
                        () -> likesService.getMyFavoriteProperties(memberId, status, page, size));

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
        verify(propertyRepository, never())
                .findByDateRange(any(LocalDate.class), any(PageRequest.class), anyBoolean());
    }
}
