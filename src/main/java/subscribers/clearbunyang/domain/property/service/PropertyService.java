package subscribers.clearbunyang.domain.property.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.domain.consultation.entity.MemberConsultation;
import subscribers.clearbunyang.domain.consultation.repository.MemberConsultationRepository;
import subscribers.clearbunyang.domain.file.entity.File;
import subscribers.clearbunyang.domain.file.model.FileRequestDTO;
import subscribers.clearbunyang.domain.file.repository.FileRepository;
import subscribers.clearbunyang.domain.likes.repository.LikesRepository;
import subscribers.clearbunyang.domain.property.entity.Area;
import subscribers.clearbunyang.domain.property.entity.Keyword;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordType;
import subscribers.clearbunyang.domain.property.exception.JsonConversionException;
import subscribers.clearbunyang.domain.property.model.request.AreaRequestDTO;
import subscribers.clearbunyang.domain.property.model.request.ConsultationRequestDTO;
import subscribers.clearbunyang.domain.property.model.request.KeywordRequestDTO;
import subscribers.clearbunyang.domain.property.model.request.PropertyRequestDTO;
import subscribers.clearbunyang.domain.property.model.response.KeywordResponseDTO;
import subscribers.clearbunyang.domain.property.model.response.MyPropertyCardResponseDTO;
import subscribers.clearbunyang.domain.property.model.response.PropertyDetailsResponseDTO;
import subscribers.clearbunyang.domain.property.repository.AreaRepository;
import subscribers.clearbunyang.domain.property.repository.KeywordRepository;
import subscribers.clearbunyang.domain.property.repository.PropertyRepository;
import subscribers.clearbunyang.domain.user.entity.Admin;
import subscribers.clearbunyang.domain.user.entity.Member;
import subscribers.clearbunyang.domain.user.repository.AdminRepository;
import subscribers.clearbunyang.domain.user.repository.MemberRepository;
import subscribers.clearbunyang.global.model.PagedDto;

@RequiredArgsConstructor
@Service
public class PropertyService {
    private final KeywordRepository keywordRepository;
    private final PropertyRepository propertyRepository;
    private final AreaRepository areaRepository;
    private final AdminRepository adminRepository;
    private final FileRepository fileRepository;
    private final MemberConsultationRepository memberConsultationRepository;
    private final MemberRepository memberRepository;
    private final LikesRepository likesRepository;
    private final ObjectMapper objectMapper;

    /**
     * 키워드들을 저장하는 메소드
     *
     * @param keywordRequestDTOS
     * @param property
     */
    public void saveKeywords(List<KeywordRequestDTO> keywordRequestDTOS, Property property) {
        List<Keyword> searchableKeywords = new ArrayList<>();
        List<KeywordRequestDTO> nonSearchableKeywords = new ArrayList<>();

        for (KeywordRequestDTO keywordRequestDTO : keywordRequestDTOS) {
            if (keywordRequestDTO.getSearchEnabled()) {
                Keyword keyword =
                        Keyword.toEntity(
                                keywordRequestDTO, objectToJson(keywordRequestDTO), property);
                searchableKeywords.add(keyword);
            } else {
                nonSearchableKeywords.add(keywordRequestDTO);
            }
        }
        keywordRepository.saveAll(searchableKeywords);

        if (!nonSearchableKeywords.isEmpty()) {
            saveNonSearchableKeywordsAsJson(nonSearchableKeywords, property);
        }
    }

    /**
     * 검색 키워드에 해당되지 않은 키워드들을 모아서 하나의 레코드에 저장하는 메소드
     *
     * @param nonSearchableKeywords 검색 키워드에 해당되지 않은 키워드들을 모아놓은 리스트
     * @param property
     */
    private void saveNonSearchableKeywordsAsJson(
            List<KeywordRequestDTO> nonSearchableKeywords, Property property) {
        Keyword keyword = Keyword.toEntity(objectToJson(nonSearchableKeywords), property);
        keywordRepository.save(keyword);
    }

    /**
     * json 형식의 데이터를 string으로 직렬화하는 메소드
     *
     * @param input json 형식의 데이터
     * @return
     */
    private String objectToJson(Object input) {
        try {
            return objectMapper.writeValueAsString(input);
        } catch (Exception e) {
            e.printStackTrace();
            throw new JsonConversionException();
        }
    }

    /**
     * 세대 면적을 저장하는 메소드
     *
     * @param areaRequestDTOS
     * @param property
     */
    private void saveAreas(List<AreaRequestDTO> areaRequestDTOS, Property property) {
        List<Area> areas =
                areaRequestDTOS.stream()
                        .map(areaDTO -> Area.toEntity(areaDTO, property))
                        .collect(Collectors.toList());

        areaRepository.saveAll(areas);
    }

    /**
     * 파일을 저장하는 메소드
     *
     * @param fileRequestDTOS
     * @param property
     * @param admin
     */
    private void saveFiles(List<FileRequestDTO> fileRequestDTOS, Property property, Admin admin) {
        List<File> files =
                fileRequestDTOS.stream()
                        .map(fileDTO -> File.toEntity(fileDTO, property, admin))
                        .collect(Collectors.toList());

        fileRepository.saveAll(files);
    }

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
        Property property = Property.toEntity(propertyDTO, admin);
        Property savedProperty = propertyRepository.save(property);

        saveAreas(propertyDTO.getAreas(), savedProperty);
        saveFiles(propertyDTO.getFiles(), savedProperty, admin);
        saveKeywords(propertyDTO.getKeywords(), property);

        return savedProperty;
    }

    /**
     * keyword를 type(infra/benefit)에 따라 카테고리화해 리턴하는 메소드
     *
     * @param propertyId
     * @return
     */
    public Map<KeywordType, List<KeywordResponseDTO>> categorizedKeywords(Long propertyId) {
        List<Keyword> keywords = keywordRepository.findByPropertyId(propertyId);
        try {
            List<KeywordResponseDTO> keywordList = new ArrayList<>();
            for (Keyword keyword : keywords) {

                if (keyword.isSearchable()) { // 단일 객체를 읽어드림
                    keywordList.add(
                            objectMapper.readValue(
                                    keyword.getJsonValue(), KeywordResponseDTO.class));
                } else { // 리스트로 읽어 들임
                    List<KeywordResponseDTO> keywordRequestDTOS =
                            objectMapper.readValue(
                                    keyword.getJsonValue(),
                                    new TypeReference<List<KeywordResponseDTO>>() {});
                    keywordList.addAll(keywordRequestDTOS);
                }
            }

            return keywordList.stream().collect(Collectors.groupingBy(KeywordResponseDTO::getType));

        } catch (Exception e) {
            throw new JsonConversionException();
        }
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
                categorizedKeywords(propertyId);

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
    // todo 인덱스 설정하기, 커밋하기
    @Transactional(readOnly = true)
    public PagedDto<MyPropertyCardResponseDTO> getCards(int page, int size, Long adminId) {
        PageRequest pageRequest =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(Sort.Direction.DESC, "endDate", "createdAt")); // todo 정렬 기준 다시 세우기
        Page<Property> pages =
                propertyRepository.findByAdmin_Id(
                        adminId, pageRequest); // todo count 쿼리가 추가로 발생하는데 따로 jpql에서 countquery로 처리?
        List<MyPropertyCardResponseDTO> cardResponseDTO =
                pages.getContent().stream()
                        .map(it -> MyPropertyCardResponseDTO.toDTO(it))
                        .collect(Collectors.toList());
        return PagedDto.toDTO(page, size, pages.getTotalPages(), cardResponseDTO);
    }
}
