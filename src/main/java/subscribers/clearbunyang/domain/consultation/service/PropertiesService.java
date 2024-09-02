package subscribers.clearbunyang.domain.consultation.service;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.domain.consultation.entity.AdminConsultation;
import subscribers.clearbunyang.domain.consultation.entity.MemberConsultation;
import subscribers.clearbunyang.domain.consultation.entity.enums.Status;
import subscribers.clearbunyang.domain.consultation.entity.enums.Tier;
import subscribers.clearbunyang.domain.consultation.exception.ConsultationException;
import subscribers.clearbunyang.domain.consultation.model.request.NewCustomerAdditionRequest;
import subscribers.clearbunyang.domain.consultation.model.response.ConsultCompletedListResponse;
import subscribers.clearbunyang.domain.consultation.model.response.ConsultCompletedSummaryResponse;
import subscribers.clearbunyang.domain.consultation.model.response.ConsultPendingListResponse;
import subscribers.clearbunyang.domain.consultation.model.response.ConsultPendingSummaryResponse;
import subscribers.clearbunyang.domain.consultation.model.response.PagedDTO;
import subscribers.clearbunyang.domain.consultation.model.response.SideBarCompletedResponse;
import subscribers.clearbunyang.domain.consultation.model.response.SideBarListResponse;
import subscribers.clearbunyang.domain.consultation.model.response.SideBarPendingResponse;
import subscribers.clearbunyang.domain.consultation.model.response.SideBarSelectedPropertyResponse;
import subscribers.clearbunyang.domain.consultation.repository.AdminConsultationRepository;
import subscribers.clearbunyang.domain.consultation.repository.MemberConsultationRepository;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.repository.PropertyRepository;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;

@Slf4j
@Service
@RequiredArgsConstructor
public class PropertiesService {

    private final AdminConsultationRepository adminConsultationRepository;
    private final MemberConsultationRepository memberConsultationRepository;
    private final PropertyRepository propertyRepository;

    @Transactional
    public void createNewCustomerAddition(Long propertyId, NewCustomerAdditionRequest request) {
        validDate(LocalDate.now(), request.getPreferredAt());
        checkPhoneNumberExists(request.getPhoneNumber());
        Property property = getProperty(propertyId);

        AdminConsultation consultant = createAdminConsultation(request);

        MemberConsultation memberConsultation =
                MemberConsultation.toEntity(request, property, consultant);
        memberConsultationRepository.save(memberConsultation);
    }

    @Transactional(readOnly = true)
    public SideBarListResponse getSideBarList(Long propertyId) {
        LocalDate today = LocalDate.now();
        Pageable pageable = PageRequest.of(0, 20);

        List<SideBarPendingResponse> sideBarPendingResponse =
                getPendingPropertiesDto(today, pageable);
        List<SideBarCompletedResponse> completedSummaryResponse =
                getCompletedPropertiesDto(today, pageable);

        Property property = getProperty(propertyId);

        SideBarSelectedPropertyResponse sideBarSelectedPropertyResponse =
                SideBarSelectedPropertyResponse.toDto(property);

        return SideBarListResponse.toDto(
                sideBarPendingResponse, completedSummaryResponse, sideBarSelectedPropertyResponse);
    }

    @Transactional(readOnly = true)
    public PagedDTO<ConsultPendingListResponse> getConsultPendingListResponse(
            Long propertyId,
            String search,
            String consultant,
            LocalDate preferredAt,
            int page,
            int size) {
        getPropertyId(propertyId);

        Page<AdminConsultation> adminConsultationPage =
                filterPendingConsultations(propertyId, search, consultant, preferredAt, page, size);

        List<ConsultPendingSummaryResponse> summaryResponseList =
                createSummaryResponseList(adminConsultationPage.getContent());

        int totalPages = adminConsultationPage.getTotalPages();

        ConsultPendingListResponse consultPendingListResponse =
                ConsultPendingListResponse.toDto(summaryResponseList);

        return PagedDTO.<ConsultPendingListResponse>builder()
                .totalPages(totalPages)
                .pageSize(size)
                .currentPage(page)
                .content(consultPendingListResponse)
                .build();
    }

    @Transactional(readOnly = true)
    public PagedDTO<ConsultCompletedListResponse> getConsultCompletedListResponse(
            Long propertyId,
            String search,
            Tier tier,
            String consultant,
            LocalDate preferredAt,
            int page,
            int size) {
        getPropertyId(propertyId);

        Page<AdminConsultation> adminConsultationPage =
                filterCompletedConsultations(
                        propertyId, search, tier, consultant, preferredAt, page, size);

        List<ConsultCompletedSummaryResponse> summaryList =
                adminConsultationPage.getContent().stream()
                        .map(ConsultCompletedSummaryResponse::toDto)
                        .collect(Collectors.toList());

        int totalPages = adminConsultationPage.getTotalPages();

        ConsultCompletedListResponse counselCompletedListResponse =
                ConsultCompletedListResponse.toDto(summaryList);

        return PagedDTO.<ConsultCompletedListResponse>builder()
                .totalPages(totalPages)
                .pageSize(size)
                .currentPage(page)
                .content(counselCompletedListResponse)
                .build();
    }

    private Property getProperty(Long propertyId) {
        return propertyRepository.getById(propertyId);
    }

    private Long getPropertyId(Long id) {
        return propertyRepository.getIdById(id);
    }

    private void validDate(LocalDate today, LocalDate preferredAt) {
        if (today.isAfter(preferredAt) || preferredAt.isBefore(today)) {
            throw new ConsultationException(ErrorCode.DATETIME_INVALID);
        }
    }

    private void checkPhoneNumberExists(String phoneNumber) {
        memberConsultationRepository.checkPhoneNumberExists(phoneNumber);
    }

    // search 키워드가 ["name 010123412354"] 일 때 분리
    private String[] splitSearch(String search) {
        if (search == null || search.isEmpty()) {
            return new String[] {"", ""};
        }

        String[] parts = search.split("\\s+");
        String name = parts.length > 0 ? parts[0] : "";
        String phoneNumber = parts.length > 1 ? parts[1] : "";
        return new String[] {name, phoneNumber};
    }

    private AdminConsultation createAdminConsultation(NewCustomerAdditionRequest request) {
        AdminConsultation consultant = new AdminConsultation();
        validateTier(request);
        consultant.setConsultant(request.getConsultant());
        if (request.getTier() != null) {
            consultant.setTier(request.getTier());
        }
        return adminConsultationRepository.save(consultant);
    }

    private void validateTier(NewCustomerAdditionRequest request) {
        // Status가 PENDING일 때, tier가 존재하면 예외 발생
        if (request.getStatus().equals(Status.PENDING)) {
            if (request.getTier() != null) {
                // TIER 이외의 값 입력시 에러,이게 없으면 pending 일 때 이상한 값(R,3,6) 입력이 됨
                if (!Tier.isValidStatus(request.getTier().name())) {
                    throw new ConsultationException(ErrorCode.TIER_CANNOT_BE_SPECIFIED);
                } // TIER 입력시 에러
                throw new ConsultationException(ErrorCode.TIER_CANNOT_BE_SPECIFIED);
            }
        } else if (request.getStatus().equals(Status.COMPLETED)) {
            if (request.getTier() == null) {
                // COMPLETED 일 때 TIER가 비어있는 경우 에러
                throw new ConsultationException(ErrorCode.INVALID_INPUT_VALUE);
            }
        }
    }

    private List<SideBarPendingResponse> getPendingPropertiesDto(
            LocalDate today, Pageable pageable) {
        return propertyRepository.getPendingPropertiesDto(today, pageable).stream()
                .map(
                        propertyDto ->
                                SideBarPendingResponse.builder()
                                        .id(propertyDto.getId())
                                        .name(propertyDto.getName())
                                        .build())
                .collect(Collectors.toList());
    }

    private List<SideBarCompletedResponse> getCompletedPropertiesDto(
            LocalDate today, Pageable pageable) {
        return propertyRepository.getCompletedPropertiesDto(today, pageable).stream()
                .map(
                        propertyDateDto ->
                                SideBarCompletedResponse.builder()
                                        .id(propertyDateDto.getId())
                                        .name(propertyDateDto.getName())
                                        .build())
                .collect(Collectors.toList());
    }

    private List<ConsultPendingSummaryResponse> createSummaryResponseList(
            List<AdminConsultation> memberConsultations) {
        return memberConsultations.stream()
                .map(
                        mc -> {
                            Boolean extra =
                                    memberConsultationRepository.checkExtraConsultation(
                                            mc.getMemberConsultation().getMedium().name());
                            return ConsultPendingSummaryResponse.toDto(
                                    mc.getMemberConsultation(), extra);
                        })
                .collect(Collectors.toList());
    }

    private Page<AdminConsultation> filterPendingConsultations(
            Long propertyId,
            String search,
            String consultant,
            LocalDate preferredAt,
            int page,
            int size) {

        String[] searchParts = splitSearch(search);
        String name = searchParts[0];
        String phoneNumber = searchParts[1];

        return adminConsultationRepository.findByPropertyIdAndPendingAndFilters(
                propertyId,
                Status.PENDING,
                name,
                phoneNumber,
                preferredAt,
                consultant,
                PageRequest.of(page, size));
    }

    private Page<AdminConsultation> filterCompletedConsultations(
            Long propertyId,
            String search,
            Tier tier,
            String consultant,
            LocalDate preferredAt,
            int page,
            int size) {

        String[] searchParts = splitSearch(search);
        String name = searchParts[0];
        String phoneNumber = searchParts[1];

        return adminConsultationRepository.findByPropertyIdAndCompletedAndFilters(
                propertyId,
                Status.COMPLETED,
                name,
                phoneNumber,
                tier,
                consultant,
                preferredAt,
                PageRequest.of(page, size));
    }
}
