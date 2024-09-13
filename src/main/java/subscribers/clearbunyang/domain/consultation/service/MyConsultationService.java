package subscribers.clearbunyang.domain.consultation.service;


import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.domain.consultation.entity.MemberConsultation;
import subscribers.clearbunyang.domain.consultation.model.myConsultations.ConsultationPagedResponse;
import subscribers.clearbunyang.domain.consultation.model.myConsultations.MyPendingConsultationsResponse;
import subscribers.clearbunyang.domain.consultation.repository.MemberConsultationRepository;
import subscribers.clearbunyang.domain.user.repository.MemberRepository;
import subscribers.clearbunyang.global.exception.Invalid.InvalidValueException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;
import subscribers.clearbunyang.global.model.PagedDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyConsultationService {

    private final MemberConsultationRepository memberConsultationRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public PagedDto<ConsultationPagedResponse> getMyPendingConsultationsList(
            Long userId, String search, int page, int size) {
        validateUserId(userId);

        String searchParam = (search == null || search.trim().isEmpty()) ? "" : search.trim();

        List<MemberConsultation> consultations =
                memberConsultationRepository.findPendingConsultationsByUserIdAndSearch(
                        userId, searchParam);

        int totalCount = memberConsultationRepository.countConsultationsByUserId(userId);

        List<MyPendingConsultationsResponse> responses =
                consultations.stream()
                        .map(MyPendingConsultationsResponse::toDto)
                        .collect(Collectors.toList());

        int totalPages = (totalCount + size - 1) / size;

        PagedDto<List<MyPendingConsultationsResponse>> pagedDto =
                PagedDto.<List<MyPendingConsultationsResponse>>builder()
                        .totalPages(totalPages)
                        .pageSize(size)
                        .currentPage(page)
                        .contents(responses)
                        .build();

        return pagedDto;
    }

    @Transactional
    public ConsultationPagedResponse getMyCompletedConsultationsList(
            Long userId, String search, int page, int size) {
        validateUserId(userId);

        String searchParam = (search == null || search.trim().isEmpty()) ? "" : search.trim();
        List<MemberConsultation> consultations =
                memberConsultationRepository.findCompletedConsultationsByUserIdAndSearch(
                        userId, searchParam);

        int totalCount = memberConsultationRepository.countConsultationsByUserId(userId);

        List<MyPendingConsultationsResponse> responses =
                consultations.stream()
                        .map(MyPendingConsultationsResponse::toDto)
                        .collect(Collectors.toList());

        int totalPages = (totalCount + size - 1) / size;

        PagedDto<List<MyPendingConsultationsResponse>> pagedDto =
                PagedDto.<List<MyPendingConsultationsResponse>>builder()
                        .totalPages(totalPages)
                        .pageSize(size)
                        .currentPage(page)
                        .content(responses)
                        .build();

        return ConsultationPagedResponse.builder()
                .totalCount(totalCount)
                .pagedData(pagedDto)
                .build();
    }

    private Member validateUserId(Long userId) {
        return memberRepository
                .findById(userId)
                .orElseThrow(() -> new InvalidValueException(ErrorCode.USER_NOT_FOUND));
    }
}
