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
        getProperty(propertyId);
        List<AdminConsultation> adminConsultation =
                adminConsultationRepository.findAllConsultantByPropertyId(propertyId);

        List<ConsultantResponse> consultantListResponses =
                adminConsultation.stream()
                        .map(ConsultantResponse::toDto)
                        .distinct() // 중복제거
                        .collect(Collectors.toList());

        return ConsultantListResponse.toDto(consultantListResponses);
    }

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

        AdminConsultation adminConsultation =
                AdminConsultation.toEntity(request, memberConsultation);

        AdminConsultation saved = adminConsultationRepository.save(adminConsultation);

        return AdminConsultResponse.toDto(saved);
    }

    private AdminConsultation getAdminConsultation(Long id) {
        return adminConsultationRepository.getById(id);
    }

    private MemberConsultation getMemberConsultation(Long id) {
        return memberConsultationRepository.getById(id);
    }

    private Property getProperty(Long propertyId) {
        return propertyRepository.getById(propertyId);
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
