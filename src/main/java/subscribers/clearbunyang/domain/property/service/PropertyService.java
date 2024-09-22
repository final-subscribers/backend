package subscribers.clearbunyang.domain.property.service;


import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.domain.auth.entity.Admin;
import subscribers.clearbunyang.domain.auth.entity.Member;
import subscribers.clearbunyang.domain.auth.repository.AdminRepository;
import subscribers.clearbunyang.domain.auth.repository.MemberRepository;
import subscribers.clearbunyang.domain.consultation.entity.AdminConsultation;
import subscribers.clearbunyang.domain.consultation.entity.MemberConsultation;
import subscribers.clearbunyang.domain.consultation.entity.enums.Status;
import subscribers.clearbunyang.domain.consultation.repository.AdminConsultationRepository;
import subscribers.clearbunyang.domain.consultation.repository.MemberConsultationRepository;
import subscribers.clearbunyang.domain.likes.repository.LikesRepository;
import subscribers.clearbunyang.domain.likes.service.LikesService;
import subscribers.clearbunyang.domain.property.dto.request.*;
import subscribers.clearbunyang.domain.property.dto.response.KeywordResponse;
import subscribers.clearbunyang.domain.property.dto.response.MyPropertyCardResponse;
import subscribers.clearbunyang.domain.property.dto.response.MyPropertyTableResponse;
import subscribers.clearbunyang.domain.property.dto.response.PropertyDetailsResponse;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordType;
import subscribers.clearbunyang.domain.property.repository.AreaRepository;
import subscribers.clearbunyang.domain.property.repository.KeywordRepository;
import subscribers.clearbunyang.domain.property.repository.PropertyRepository;
import subscribers.clearbunyang.global.dto.PagedDto;
import subscribers.clearbunyang.global.exception.InvalidValueException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;
import subscribers.clearbunyang.global.file.dto.FileRequestDTO;
import subscribers.clearbunyang.global.file.entity.enums.FileType;
import subscribers.clearbunyang.global.file.repository.FileRepository;
import subscribers.clearbunyang.global.file.service.FileService;

@RequiredArgsConstructor
@Service
public class PropertyService {
    private final PropertyRepository propertyRepository;
    private final AdminRepository adminRepository;
    private final MemberConsultationRepository memberConsultationRepository;
    private final MemberRepository memberRepository;
    private final LikesRepository likesRepository;
    private final FileService fileService;
    private final KeywordService keywordService;
    private final AreaService areaService;
    private final AdminConsultationRepository adminConsultationRepository;
    private final KeywordRepository keywordRepository;
    private final AreaRepository areaRepository;
    private final FileRepository fileRepository;
    private final LikesService likesService;

    /**
     * 물건을 저장하는 메소드
     *
     * @param propertyDTO
     * @param adminId
     * @return
     */
    @Transactional
    // @CacheEvict(value = "sidebarList", allEntries = true)
    public Property saveProperty(PropertySaveRequest propertyDTO, Long adminId) {
        Admin admin = adminRepository.findAdminById(adminId);
        String imageUrl =
                propertyDTO.getFiles().stream()
                        .filter(file -> file.getType() == FileType.PROPERTY_IMAGE)
                        .map(FileRequestDTO::getUrl)
                        .findFirst()
                        .orElseThrow(
                                () -> new InvalidValueException(ErrorCode.FILE_TYPE_NOT_FOUND));
        AreaRequest smallestArea =
                propertyDTO.getAreas().stream()
                        .min(Comparator.comparing(AreaRequest::getSquareMeter))
                        .orElseThrow(() -> new InvalidValueException(ErrorCode.AREA_NOT_FOUND));

        Property property = Property.toEntity(propertyDTO, admin);
        property.setDenormalizationFields(
                imageUrl,
                smallestArea.getPrice(),
                smallestArea.getDiscountPrice(),
                smallestArea.getDiscountPercent());
        Property savedProperty = propertyRepository.save(property);

        areaService.saveAreas(propertyDTO.getAreas(), savedProperty);
        fileService.saveFiles(propertyDTO.getFiles(), savedProperty);
        keywordService.saveKeywords(propertyDTO.getKeywords(), property);
        return savedProperty;
    }

    /**
     * 상담을 등록하는 메소드
     *
     * @param propertyId
     * @param requestDTO
     * @param memberId
     */
    @Transactional
    /* @CacheEvict(
    value = {"ConsultPendingList", "ConsultCompletedList"},
    allEntries = true)*/
    public MemberConsultation saveConsultation(
            Long propertyId, MemberConsultationRequest requestDTO, Long memberId) {
        Property property = propertyRepository.findPropertyById(propertyId);
        Member member = (memberId != null) ? memberRepository.findMemberById(memberId) : null;
        AdminConsultation savedAdminConsultation =
                adminConsultationRepository.save(AdminConsultation.builder().build());

        MemberConsultation memberConsultation =
                MemberConsultation.toEntity(requestDTO, property, member, savedAdminConsultation);
        return memberConsultationRepository.save(memberConsultation);
    }

    /**
     * 매물 상세 정보를 리턴하는 매소드
     *
     * @param propertyId
     * @param memberId
     * @return
     */
    @Transactional(readOnly = true)
    public PropertyDetailsResponse getPropertyDetails(Long propertyId, Long memberId) {
        Property property = propertyRepository.getByPropertyUsingFetchJoin(propertyId);
        boolean likesExisted = likesService.isLiked(memberId, propertyId);

        Map<KeywordType, List<KeywordResponse>> categorizedKeywords =
                keywordService.categorizedKeywords(propertyId);

        return PropertyDetailsResponse.toDTO(property, categorizedKeywords, likesExisted);
    }

    /**
     * 내가 등록한 매물의 첫번째 페이지네이션을 리턴하는 메소드
     *
     * @param page 현재 페이지(0부터 시작)
     * @param size 한 페이지당 보일 객체의 수
     * @param adminId
     * @return
     */
    @Transactional(readOnly = true)
    public PagedDto<MyPropertyCardResponse> getCards(int page, int size, Long adminId) {
        PageRequest pageRequest =
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "endDate", "createdAt"));
        Page<Property> pages = propertyRepository.findByAdmin_Id(adminId, pageRequest);
        List<MyPropertyCardResponse> cardResponseDTO =
                pages.getContent().stream()
                        .map(it -> MyPropertyCardResponse.toDTO(it))
                        .collect(Collectors.toList());
        return PagedDto.toDTO(page, size, pages.getTotalPages(), cardResponseDTO);
    }

    /**
     * 내가 등록한 매물의 두번째 페이지네이션을 리턴하는 메소드
     *
     * @param page 현재 페이지(0부터 시작)
     * @param size 한 페이지당 보일 객체의 수
     * @param adminId
     * @return
     */
    @Transactional(readOnly = true)
    public PagedDto<MyPropertyTableResponse> getTables(int page, int size, Long adminId) {
        PageRequest pageRequest =
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "endDate", "createdAt"));
        Page<Property> pages = propertyRepository.findByAdmin_Id(adminId, pageRequest);
        List<Long> propertyIds =
                pages.getContent().stream().map(Property::getId).collect(Collectors.toList());
        List<Object[]> objects =
                memberConsultationRepository.countPendingByPropertyIds(propertyIds, Status.PENDING);

        Map<Long, Long> pendingCountsMap =
                objects.stream()
                        .collect(
                                Collectors.toMap(
                                        obj -> (Long) obj[0], // Property ID
                                        obj -> (Long) obj[1] // Pending count
                                        ));

        List<MyPropertyTableResponse> cardResponseDTO =
                pages.getContent().stream()
                        .map(
                                property -> {
                                    Long pendingCount =
                                            pendingCountsMap.getOrDefault(property.getId(), 0L);
                                    return MyPropertyTableResponse.toDTO(property, pendingCount);
                                })
                        .collect(Collectors.toList());
        return PagedDto.toDTO(page, size, pages.getTotalPages(), cardResponseDTO);
    }

    /**
     * 매물을 삭제하는 메소드
     *
     * @param propertyId
     * @param adminId
     */
    @Transactional
    public void deleteProperty(Long propertyId, Long adminId) {
        propertyRepository.existsByIdAndAdmin_id(propertyId, adminId);

        keywordRepository.deleteByPropertyId(propertyId);
        areaRepository.deleteByPropertyId(propertyId);
        fileRepository.deleteByPropertyId(propertyId);
        likesRepository.deleteByPropertyId(propertyId);
        propertyRepository.deletePropertyById(propertyId);
    }

    // @CacheEvict(value = "sidebarList", allEntries = true)
    @Transactional
    public Property updateProperty(
            Long propertyId, PropertyUpdateRequest requestDTO, Long adminId) {
        propertyRepository.existsByIdAndAdmin_id(propertyId, adminId);
        Property property = propertyRepository.findPropertyById(propertyId);

        fileRepository.deleteByPropertyId(propertyId);
        List<FileRequestDTO> files = new ArrayList<>();
        files.add(requestDTO.getPropertyImage());
        files.add(requestDTO.getSupplyInformation());
        if (requestDTO.getMarketing() != null) files.add(requestDTO.getMarketing());

        keywordRepository.deleteByPropertyId(propertyId);
        List<KeywordRequest> keywords = new ArrayList<>();
        keywords.addAll(requestDTO.getBenefit());
        keywords.addAll(requestDTO.getInfra());
        keywordService.saveKeywords(keywords, property);

        areaRepository.deleteByPropertyId(propertyId);
        areaService.saveAreas(requestDTO.getAreas(), property);
        fileService.saveFiles(files, property);

        property.update(requestDTO);

        String imageUrl = requestDTO.getPropertyImage().getUrl();
        AreaRequest smallestArea =
                requestDTO.getAreas().stream()
                        .min(Comparator.comparing(AreaRequest::getSquareMeter))
                        .orElseThrow(() -> new InvalidValueException(ErrorCode.AREA_NOT_FOUND));
        property.setDenormalizationFields(
                imageUrl,
                smallestArea.getPrice(),
                smallestArea.getDiscountPrice(),
                smallestArea.getDiscountPercent());

        return propertyRepository.save(property);
    }
}
