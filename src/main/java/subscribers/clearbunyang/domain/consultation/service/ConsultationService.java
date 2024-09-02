package subscribers.clearbunyang.domain.consultation.service;


import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.domain.consultation.entity.AdminConsultation;
import subscribers.clearbunyang.domain.consultation.entity.MemberConsultation;
import subscribers.clearbunyang.domain.consultation.entity.enums.Status;
import subscribers.clearbunyang.domain.consultation.exception.ConsultationException;
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
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;

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
            throw new ConsultationException(ErrorCode.NOT_FOUND);
        }

        return ConsultCompletedResponse.toDto(adminConsultation);
    }

    @Transactional(readOnly = true) // admin으로 받으면 쿼리가 너무 길어짐 (admin에서 member 찾아서 extra 체크히고..)
    public ConsultPendingResponse getConsultPendingResponse(Long memberConsultationId) {
        MemberConsultation memberConsultation = getMemberConsultation(memberConsultationId);

        if (memberConsultation.getStatus() != Status.PENDING) {
            throw new ConsultationException(ErrorCode.NOT_FOUND);
        }

        Boolean extra =
                memberConsultationRepository.checkExtraConsultation(
                        memberConsultation.getMedium().name());

        return ConsultPendingResponse.toDto(memberConsultation, extra);
    }

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

    // 동시에 같은 상담에 대해 상담사 이름 변경을 하려고 할 때 분산락 적용
    @DistributedLock(key = "#adminConsultationId")
    @Transactional
    public ConsultantResponse changeConsultant(Long adminConsultationId, String consultant) {
        AdminConsultation adminConsultation = getAdminConsultation(adminConsultationId);
        Property property = adminConsultation.getMemberConsultation().getProperty();

        validateConsultantExists(property.getId(), consultant);

        adminConsultation.setConsultant(consultant);

        return ConsultantResponse.toDto(consultant);
    }

    @Transactional
    public ConsultCompletedResponse updateConsultMessage(Long adminConsultationId, String message) {
        AdminConsultation adminConsultation = getAdminConsultation(adminConsultationId);
        getMemberConsultation(adminConsultation.getMemberConsultation().getId());

        if (adminConsultation.getMemberConsultation().getStatus() != Status.COMPLETED) {
            throw new ConsultationException(ErrorCode.NOT_FOUND);
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
            throw new ConsultationException(ErrorCode.NOT_FOUND);
        }
    }

    private void validateRequest(ConsultRequest request) {
        if (request.getTier() == null) {
            throw new ConsultationException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }
}
