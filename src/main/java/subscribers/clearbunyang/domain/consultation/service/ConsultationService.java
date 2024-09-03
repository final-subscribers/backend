package subscribers.clearbunyang.domain.consultation.service;


import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.domain.consultation.entity.AdminConsultation;
import subscribers.clearbunyang.domain.consultation.entity.MemberConsultation;
import subscribers.clearbunyang.domain.consultation.entity.enums.Status;
import subscribers.clearbunyang.domain.consultation.model.request.ConsultRequest;
import subscribers.clearbunyang.domain.consultation.model.response.AdminConsultResponse;
import subscribers.clearbunyang.domain.consultation.model.response.ConsultCompletedResponse;
import subscribers.clearbunyang.domain.consultation.model.response.ConsultPendingResponse;
import subscribers.clearbunyang.domain.consultation.model.response.ConsultantListResponse;
import subscribers.clearbunyang.domain.consultation.model.response.ConsultantResponse;
import subscribers.clearbunyang.domain.consultation.repository.AdminConsultationRepository;
import subscribers.clearbunyang.domain.consultation.repository.MemberConsultationRepository;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.repository.PropertyRepository;
import subscribers.clearbunyang.global.annotation.DistributedLock;
import subscribers.clearbunyang.global.exception.Invalid.InvalidValueException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;
// TODO customException 으로 변경

@Service
@RequiredArgsConstructor
public class ConsultationService {

    private final AdminConsultationRepository adminConsultationRepository;
    private final MemberConsultationRepository memberConsultationRepository;
    private final PropertyRepository propertyRepository;

    @Transactional(readOnly = true)
    public ConsultCompletedResponse getConsultCompletedResponse(Long adminConsultationId) {
        AdminConsultation adminConsultation = getAdminConsultation(adminConsultationId);

        if (adminConsultation.getMemberConsultation().getStatus() != Status.COMPLETED) {
            throw new InvalidValueException(ErrorCode.NOT_FOUND);
        }

        return ConsultCompletedResponse.toDto(adminConsultation);
    }

    @Transactional(readOnly = true) // admin으로 받으면 쿼리가 너무 길어짐 (admin에서 member 찾아서 extra 체크히고..)
    public ConsultPendingResponse getConsultPendingResponse(Long memberConsultationId) {
        MemberConsultation memberConsultation = getMemberConsultation(memberConsultationId);

        if (memberConsultation.getStatus() != Status.PENDING) {
            throw new InvalidValueException(ErrorCode.NOT_FOUND);
        }

        Boolean extra =
                memberConsultationRepository.checkExtraConsultation(
                        memberConsultation.getMedium().name());

        return ConsultPendingResponse.toDto(memberConsultation, extra);
    }

    // TODO LMS 외 고객은 배정받은 상담사만 보이게
    @Transactional(readOnly = true)
    public ConsultantListResponse getConsultants(Long propertyId) {
        getPropertyId(propertyId);
        List<AdminConsultation> adminConsultation =
                adminConsultationRepository.findAllConsultantByPropertyId(propertyId);

        List<ConsultantResponse> consultantListResponses =
                adminConsultation.stream()
                        .map(ConsultantResponse::toDto)
                        .distinct() // 중복제거
                        .collect(Collectors.toList());

        return ConsultantListResponse.toDto(consultantListResponses);
    }

    /**
     * 동시성 제어 기능을 사용하려면 상담사 변경 기능은 없어도 괜찮을거 같습니다! phone, none 일시 상담사 변경 불가 -> lms 일 시 한 번 등록하고, 변경
     * 불가 덮어쓰기 불가 -> 여기서 동시성 제어 adminconsultationID 존재 유무로
     */
    @DistributedLock(key = "#adminConsultationId")
    @Transactional
    public ConsultantResponse registerConsultant(Long adminConsultationId, String consultant) {
        AdminConsultation adminConsultation = getAdminConsultation(adminConsultationId);

        Property property = adminConsultation.getMemberConsultation().getProperty();

        validateConsultantExists(property.getId(), consultant);

        String existingConsultant = adminConsultation.getConsultant();
        if (!existingConsultant.isEmpty()) {
            throw new InvalidValueException(ErrorCode.UNABLE_TO_CHANGE_CONSULTANT);
        }

        adminConsultation.setConsultant(consultant);

        return ConsultantResponse.toDto(consultant);
    }

    @Transactional
    public ConsultCompletedResponse updateConsultMessage(Long adminConsultationId, String message) {
        AdminConsultation adminConsultation = getAdminConsultation(adminConsultationId);
        getMemberConsultation(adminConsultation.getMemberConsultation().getId());

        if (adminConsultation.getMemberConsultation().getStatus() != Status.COMPLETED) {
            throw new InvalidValueException(ErrorCode.NOT_FOUND);
        }

        adminConsultation.setMessage(message);

        return ConsultCompletedResponse.toDto(adminConsultation);
    }

    @Transactional
    public AdminConsultResponse createAdminConsult(
            Long memberConsultationId, ConsultRequest request) {
        MemberConsultation memberConsultation = getMemberConsultation(memberConsultationId);
        validateRequest(request);

        memberConsultation.setStatus(request.getStatus());
        AdminConsultation adminConsultation = memberConsultation.getAdminConsultation();
        adminConsultation.update(request, memberConsultation);

        return AdminConsultResponse.toDto(adminConsultation);
    }

    private AdminConsultation getAdminConsultation(Long id) {
        return adminConsultationRepository.getById(id);
    }

    private MemberConsultation getMemberConsultation(Long id) {
        return memberConsultationRepository.getById(id);
    }

    private Long getPropertyId(Long id) {
        return propertyRepository.getIdById(id);
    }

    private void validateConsultantExists(Long propertyId, String consultant) {
        List<AdminConsultation> adminConsultations =
                adminConsultationRepository.findByPropertyId(propertyId);
        boolean existsConsultant =
                adminConsultations.stream().anyMatch(ac -> ac.getConsultant().equals(consultant));

        if (!existsConsultant) {
            throw new InvalidValueException(ErrorCode.NOT_FOUND);
        }
    }

    private void validateRequest(ConsultRequest request) {
        if (request.getTier() == null) {
            throw new InvalidValueException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }
}
