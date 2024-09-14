package subscribers.clearbunyang.domain.property.service;


import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.domain.consultation.entity.MemberConsultation;
import subscribers.clearbunyang.domain.consultation.entity.enums.Status;
import subscribers.clearbunyang.domain.consultation.repository.MemberConsultationRepository;
import subscribers.clearbunyang.domain.file.entity.enums.FileType;
import subscribers.clearbunyang.domain.file.model.FileRequestDTO;
import subscribers.clearbunyang.domain.file.service.FileService;
import subscribers.clearbunyang.domain.likes.repository.LikesRepository;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordType;
import subscribers.clearbunyang.domain.property.model.request.AreaRequestDTO;
import subscribers.clearbunyang.domain.property.model.request.ConsultationRequestDTO;
import subscribers.clearbunyang.domain.property.model.request.PropertyRequestDTO;
import subscribers.clearbunyang.domain.property.model.response.KeywordResponseDTO;
import subscribers.clearbunyang.domain.property.model.response.MyPropertyCardResponseDTO;
import subscribers.clearbunyang.domain.property.model.response.MyPropertyTableResponseDTO;
import subscribers.clearbunyang.domain.property.model.response.PropertyDetailsResponseDTO;
import subscribers.clearbunyang.domain.property.repository.PropertyRepository;
import subscribers.clearbunyang.domain.user.entity.Admin;
import subscribers.clearbunyang.domain.user.entity.Member;
import subscribers.clearbunyang.domain.user.repository.AdminRepository;
import subscribers.clearbunyang.domain.user.repository.MemberRepository;
import subscribers.clearbunyang.global.exception.Invalid.InvalidValueException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;
import subscribers.clearbunyang.global.model.PagedDto;

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

    /**
     * 물건을 저장하는 메소드
     *
     * @param propertyDTO
     * @param adminId
     * @return
     */
    @Transactional
    public Property saveProperty(PropertyRequestDTO propertyDTO, Long adminId) {
        Admin admin = adminRepository.findAdminById(adminId);
        String imageUrl =
                propertyDTO.getFiles().stream()
                        .filter(file -> file.getType() == FileType.PROPERTY_IMAGE)
                        .map(FileRequestDTO::getUrl)
                        .findFirst()
                        .orElseThrow(
                                () -> new InvalidValueException(ErrorCode.FILE_TYPE_NOT_FOUND));
        AreaRequestDTO smallestArea =
                propertyDTO.getAreas().stream()
                        .min(Comparator.comparing(AreaRequestDTO::getSquareMeter))
                        .orElseThrow(() -> new InvalidValueException(ErrorCode.AREA_NOT_FOUND));

        Property property = Property.toEntity(propertyDTO, admin);
        property.setDenormalizationFields(
                imageUrl,
                smallestArea.getPrice(),
                smallestArea.getDiscountPrice(),
                smallestArea.getDiscountPercent());
        Property savedProperty = propertyRepository.save(property);

        areaService.saveAreas(propertyDTO.getAreas(), savedProperty);
        fileService.saveFiles(propertyDTO.getFiles(), savedProperty, admin);
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
    public void saveConsultation(
            Long propertyId, ConsultationRequestDTO requestDTO, Long memberId) {
        Property property = propertyRepository.findPropertyById(propertyId);
        Member member = (memberId != null) ? memberRepository.findMemberById(memberId) : null;
        MemberConsultation memberConsultation =
                MemberConsultation.toEntity(requestDTO, property, member);
        memberConsultationRepository.save(memberConsultation);
    }

    /**
     * 매물 상세 정보를 리턴하는 매소드
     *
     * @param propertyId
     * @param memberId
     * @return
     */
    @Transactional(readOnly = true)
    public PropertyDetailsResponseDTO getPropertyDetails(Long propertyId, Long memberId) {
        Property property = propertyRepository.getByPropertyUsingFetchJoin(propertyId);
        boolean likesExisted = false;
        if (memberId != null) {
            likesExisted = likesRepository.existsByMemberIdAndPropertyId(memberId, propertyId);
        }

        Map<KeywordType, List<KeywordResponseDTO>> categorizedKeywords =
                keywordService.categorizedKeywords(propertyId);

        return PropertyDetailsResponseDTO.toDTO(property, categorizedKeywords, likesExisted);
    }

    /**
     * 내가 등록한 매물의 첫번째 페이지네이션을 리턴하는 메소드
     *
     * @param page 현재 페이지(0부터 시작)
     * @param size 한 페이지당 보일 객체의 수
     * @param adminId
     * @return
     */
    // todo 인덱스 설정하기
    @Transactional(readOnly = true)
    public PagedDto<MyPropertyCardResponseDTO> getCards(int page, int size, Long adminId) {
        PageRequest pageRequest =
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "endDate", "createdAt"));
        Page<Property> pages = propertyRepository.findByAdmin_Id(adminId, pageRequest);
        List<MyPropertyCardResponseDTO> cardResponseDTO =
                pages.getContent().stream()
                        .map(it -> MyPropertyCardResponseDTO.toDTO(it))
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
    public PagedDto<MyPropertyTableResponseDTO> getTables(int page, int size, Long adminId) {
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

        List<MyPropertyTableResponseDTO> cardResponseDTO =
                pages.getContent().stream()
                        .map(
                                property -> {
                                    Long pendingCount =
                                            pendingCountsMap.getOrDefault(property.getId(), 0L);
                                    return MyPropertyTableResponseDTO.toDTO(property, pendingCount);
                                })
                        .collect(Collectors.toList());
        return PagedDto.toDTO(page, size, pages.getTotalPages(), cardResponseDTO);
    }
}
