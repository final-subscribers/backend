package subscribers.clearbunyang.domain.consultation.service;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.domain.consultation.dto.adminConsultation.response.ConsultantResponse;
import subscribers.clearbunyang.domain.consultation.dto.adminPropertyConsultation.request.NewCustomerAdditionRequest;
import subscribers.clearbunyang.domain.consultation.dto.adminPropertyConsultation.response.ConsultCompletedListResponse;
import subscribers.clearbunyang.domain.consultation.dto.adminPropertyConsultation.response.ConsultCompletedSummaryResponse;
import subscribers.clearbunyang.domain.consultation.dto.adminPropertyConsultation.response.ConsultPendingListResponse;
import subscribers.clearbunyang.domain.consultation.dto.adminPropertyConsultation.response.ConsultPendingSummaryResponse;
import subscribers.clearbunyang.domain.consultation.dto.adminPropertyConsultation.response.SideBarCompletedResponse;
import subscribers.clearbunyang.domain.consultation.dto.adminPropertyConsultation.response.SideBarListResponse;
import subscribers.clearbunyang.domain.consultation.dto.adminPropertyConsultation.response.SideBarPendingResponse;
import subscribers.clearbunyang.domain.consultation.dto.adminPropertyConsultation.response.SideBarSelectedPropertyResponse;
import subscribers.clearbunyang.domain.consultation.entity.AdminConsultation;
import subscribers.clearbunyang.domain.consultation.entity.MemberConsultation;
import subscribers.clearbunyang.domain.consultation.entity.enums.Status;
import subscribers.clearbunyang.domain.consultation.entity.enums.Tier;
import subscribers.clearbunyang.domain.consultation.repository.AdminConsultationRepository;
import subscribers.clearbunyang.domain.consultation.repository.MemberConsultationRepository;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.repository.PropertyRepository;
import subscribers.clearbunyang.global.dto.PagedDtoWithTotalCount;
import subscribers.clearbunyang.global.exception.InvalidValueException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminPropertyConsultationService {

    private final AdminConsultationRepository adminConsultationRepository;
    private final MemberConsultationRepository memberConsultationRepository;
    private final PropertyRepository propertyRepository;

    // 상담사 고정값 TODO refactor 어디로 분리할까...
    private final List<ConsultantResponse> consultantListResponses =
            List.of(
                    new ConsultantResponse("a1-1"),
                    new ConsultantResponse("a1-2"),
                    new ConsultantResponse("a1-3"),
                    new ConsultantResponse("a1-4"),
                    new ConsultantResponse("a1-5"));

    @Transactional
    @CacheEvict(
            value = {"ConsultPendingList", "ConsultCompletedList"},
            allEntries = true)
    public void createNewCustomerAddition(Long propertyId, NewCustomerAdditionRequest request) {
        Property property = getProperty(propertyId);

        boolean isValidConsultant =
                consultantListResponses.stream()
                        .anyMatch(c -> c.getConsultant().equals(request.getConsultant()));

        if (!isValidConsultant) {
            throw new InvalidValueException(ErrorCode.BAD_REQUEST);
        }
        AdminConsultation consultant = createAdminConsultation(request);

        MemberConsultation memberConsultation =
                MemberConsultation.toEntity(request, property, consultant);
        memberConsultationRepository.save(memberConsultation);
    }

    @Cacheable(value = "sidebarList")
    @Transactional(readOnly = true)
    public SideBarListResponse getSideBarList() {
        LocalDate today = LocalDate.now();
        Pageable pageable = PageRequest.of(0, 20);

        List<SideBarPendingResponse> sideBarPendingResponse =
                getPendingPropertiesDto(today, pageable);
        List<SideBarCompletedResponse> sideBarCompletedResponses =
                getCompletedPropertiesDto(today, pageable);

        return SideBarListResponse.toDto(sideBarPendingResponse, sideBarCompletedResponses);
    }

    @Cacheable(value = "selectedSidebar", keyGenerator = "keyGenerator")
    @Transactional(readOnly = true)
    public SideBarSelectedPropertyResponse getSideBarSelectedProperty(Long propertyId) {

        Property property = getProperty(propertyId);

        return SideBarSelectedPropertyResponse.toDto(property);
    }

    @Cacheable(value = "ConsultPendingList", keyGenerator = "customKeyGenerator")
    @Transactional(readOnly = true)
    public PagedDtoWithTotalCount<ConsultPendingListResponse> getConsultPendingListResponse(
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

        ConsultPendingListResponse consultPendingListResponse =
                ConsultPendingListResponse.toDto(summaryResponseList);

        return PagedDtoWithTotalCount.toDTO(
                page,
                size,
                adminConsultationPage.getTotalPages(),
                adminConsultationPage.getTotalElements(),
                List.of(consultPendingListResponse));
    }

    @Cacheable(value = "ConsultCompletedList", keyGenerator = "customKeyGenerator")
    @Transactional(readOnly = true)
    public PagedDtoWithTotalCount<ConsultCompletedListResponse> getConsultCompletedListResponse(
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

        ConsultCompletedListResponse counselCompletedListResponse =
                ConsultCompletedListResponse.toDto(summaryList);

        return PagedDtoWithTotalCount.toDTO(
                page,
                size,
                adminConsultationPage.getTotalPages(),
                adminConsultationPage.getTotalElements(),
                List.of(counselCompletedListResponse));
    }

    private Property getProperty(Long propertyId) {
        return propertyRepository.getById(propertyId);
    }

    private Long getPropertyId(Long id) {
        return propertyRepository.getIdById(id);
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
                    throw new InvalidValueException(ErrorCode.TIER_CANNOT_BE_SPECIFIED);
                } // TIER 입력시 에러
                throw new InvalidValueException(ErrorCode.TIER_CANNOT_BE_SPECIFIED);
            }
        } else if (request.getStatus().equals(Status.COMPLETED)) {
            if (request.getTier() == null) {
                // COMPLETED 일 때 TIER가 비어있는 경우 에러
                throw new InvalidValueException(ErrorCode.INVALID_INPUT_VALUE);
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
