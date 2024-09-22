package subscribers.clearbunyang.domain.consultation.service;


import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.domain.auth.entity.Member;
import subscribers.clearbunyang.domain.auth.repository.MemberRepository;
import subscribers.clearbunyang.domain.consultation.dto.memberConsultations.ConsultationResponse;
import subscribers.clearbunyang.domain.consultation.dto.memberConsultations.MyConsultationsResponse;
import subscribers.clearbunyang.domain.consultation.entity.MemberConsultation;
import subscribers.clearbunyang.domain.consultation.entity.enums.Status;
import subscribers.clearbunyang.domain.consultation.repository.MemberConsultationRepository;
import subscribers.clearbunyang.global.dto.PagedDto;
import subscribers.clearbunyang.global.exception.InvalidValueException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberConsultationService {

    private final MemberConsultationRepository memberConsultationRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public PagedDto<ConsultationResponse> getMyPendingConsultationsList(
            Long userId, String search, int page, int size) {
        validateUserId(userId);

        PageRequest pageRequest = PageRequest.of(page, size);

        String searchParam = (search == null || search.trim().isEmpty()) ? "" : search.trim();

        Page<MemberConsultation> consultations =
                memberConsultationRepository.findConsultationsByUserIdAndSearch(
                        userId, searchParam, Status.PENDING, pageRequest);

        int totalCount =
                memberConsultationRepository.countConsultationsByUserId(userId, Status.PENDING);

        List<MyConsultationsResponse> consultationsResponses =
                consultations.stream()
                        .map(MyConsultationsResponse::toDto)
                        .collect(Collectors.toList());
        log.info(consultationsResponses.toString());

        ConsultationResponse response =
                ConsultationResponse.toDto(totalCount, consultationsResponses);

        int totalPages = (totalCount + size - 1) / size;

        return PagedDto.toDTO(page, size, totalPages, List.of(response));
    }

    @Transactional
    public PagedDto<ConsultationResponse> getMyCompletedConsultationsList(
            Long userId, String search, int page, int size) {
        validateUserId(userId);

        PageRequest pageRequest = PageRequest.of(page, size);

        String searchParam = (search == null || search.trim().isEmpty()) ? "" : search.trim();

        Page<MemberConsultation> consultations =
                memberConsultationRepository.findConsultationsByUserIdAndSearch(
                        userId, searchParam, Status.COMPLETED, pageRequest);

        int totalCount =
                memberConsultationRepository.countConsultationsByUserId(userId, Status.COMPLETED);

        List<MyConsultationsResponse> consultationsResponses =
                consultations.stream()
                        .map(MyConsultationsResponse::toDto)
                        .collect(Collectors.toList());

        ConsultationResponse response =
                ConsultationResponse.toDto(totalCount, consultationsResponses);

        int totalPages = (totalCount + size - 1) / size;

        return PagedDto.toDTO(page, size, totalPages, List.of(response));
    }

    private Member validateUserId(Long userId) {
        return memberRepository
                .findById(userId)
                .orElseThrow(() -> new InvalidValueException(ErrorCode.USER_NOT_FOUND));
    }
}
