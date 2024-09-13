/*
package subscribers.clearbunyang.domain.consultation.service;



@Slf4j
@Service
@RequiredArgsConstructor
public class MyConsultationService {

    private final MemberConsultationRepository memberConsultationRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ConsultationPagedResponse getMyPendingConsultationsList(
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
                        .content(responses)
                        .build();

        return ConsultationPagedResponse.builder()
                .totalCount(totalCount)
                .pagedData(pagedDto)
                .build();
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
*/
