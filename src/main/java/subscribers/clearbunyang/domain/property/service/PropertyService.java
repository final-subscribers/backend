package subscribers.clearbunyang.domain.property.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.domain.file.entity.File;
import subscribers.clearbunyang.domain.file.model.FileDTO;
import subscribers.clearbunyang.domain.file.repository.FileRepository;
import subscribers.clearbunyang.domain.property.entity.Area;
import subscribers.clearbunyang.domain.property.entity.Keyword;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.exception.JsonConversionException;
import subscribers.clearbunyang.domain.property.model.AreaDTO;
import subscribers.clearbunyang.domain.property.model.KeywordDTO;
import subscribers.clearbunyang.domain.property.model.PropertyRequestDTO;
import subscribers.clearbunyang.domain.property.repository.AreaRepository;
import subscribers.clearbunyang.domain.property.repository.KeywordRepository;
import subscribers.clearbunyang.domain.property.repository.PropertyRepository;
import subscribers.clearbunyang.domain.user.entity.Admin;
import subscribers.clearbunyang.domain.user.repository.AdminRepository;

@RequiredArgsConstructor
@Service
public class PropertyService {
    private final KeywordRepository keywordRepository;
    private final PropertyRepository propertyRepository;
    private final AreaRepository areaRepository;
    private final AdminRepository adminRepository;
    private final FileRepository fileRepository;

    private final ObjectMapper objectMapper;

    public void saveKeywords(List<KeywordDTO> keywords, Property property) {
        List<Keyword> searchableKeywords = new ArrayList<>();
        List<KeywordDTO> nonSearchableKeywords = new ArrayList<>();

        for (KeywordDTO keyword : keywords) {
            if (keyword.getSearchEnabled()) {
                searchableKeywords.add(toKeyword(keyword, property));
            } else {
                nonSearchableKeywords.add(keyword);
            }
        }
        keywordRepository.saveAll(searchableKeywords);
        if (!nonSearchableKeywords.isEmpty()) {
            saveNonSearchableKeywordsAsJson(nonSearchableKeywords, property);
        }
    }

    private Keyword toKeyword(KeywordDTO keywordDTO, Property property) {
        Keyword keyword =
                Keyword.builder()
                        .name(keywordDTO.getName())
                        .type(keywordDTO.getType())
                        .jsonValue(objectToJson(keywordDTO))
                        //                .isSearchable(keywordDTO.isSearchable())
                        .isSearchable(keywordDTO.getSearchEnabled())
                        .property(property)
                        .build();
        return keyword;
    }

    private void saveNonSearchableKeywordsAsJson(
            List<KeywordDTO> nonSearchableKeywords, Property property) {
        // 변환된 JSON 문자열을 새로운 Keyword 엔티티에 저장
        Keyword keyword =
                Keyword.builder()
                        .jsonValue(objectToJson(nonSearchableKeywords))
                        .isSearchable(false)
                        .property(property)
                        .build();

        keywordRepository.save(keyword);
    }

    private String objectToJson(Object input) {
        try {
            return objectMapper.writeValueAsString(input);
        } catch (Exception e) {
            e.printStackTrace();
            throw new JsonConversionException();
        }
    }

    public Map<String, List<KeywordDTO>> categorizedKeywords(Long propertyId) {
        List<Keyword> keywords = keywordRepository.findByPropertyId(propertyId);
        try {
            List<KeywordDTO> keywordList = new ArrayList<>();
            for (Keyword keyword : keywords) {

                if (keyword.isSearchable()) { // 단일 객체를 읽어드림
                    keywordList.add(
                            objectMapper.readValue(keyword.getJsonValue(), KeywordDTO.class));
                } else { // 리스트로 읽어 들임
                    List<KeywordDTO> keywordDTOS =
                            objectMapper.readValue(
                                    keyword.getJsonValue(),
                                    new TypeReference<List<KeywordDTO>>() {});
                    keywordList.addAll(keywordDTOS);
                }
            }

            // type에 따라 두 개의 리스트로 분류
            Map<String, List<KeywordDTO>> categorizedKeywords =
                    keywordList.stream().collect(Collectors.groupingBy(KeywordDTO::getType));

            return categorizedKeywords;

        } catch (Exception e) {
            throw new JsonConversionException();
        }
    }

    private void saveAreas(List<AreaDTO> areaDTOs, Property property) {
        List<Area> areas =
                areaDTOs.stream()
                        .map(
                                areaDTO ->
                                        Area.builder()
                                                .squareMeter(areaDTO.getSquareMeter())
                                                .price(areaDTO.getPrice())
                                                .discountPercent(areaDTO.getDiscountPercent())
                                                .property(property)
                                                .build())
                        .collect(Collectors.toList());

        areaRepository.saveAll(areas);
    }

    private void saveFiles(List<FileDTO> fileDTOs, Property property, Admin admin) {
        List<File> files =
                fileDTOs.stream()
                        .map(
                                fileDTO ->
                                        File.builder()
                                                .property(property)
                                                .admin(admin)
                                                .name(fileDTO.getName())
                                                .link(fileDTO.getUrl())
                                                .type(fileDTO.getType())
                                                .build())
                        .collect(Collectors.toList());

        fileRepository.saveAll(files);
    }

    @Transactional
    public Property saveProperty(PropertyRequestDTO propertyDTO) {
        Admin admin = adminRepository.findById(1L).get(); // todo 파라미터로 받기

        Property property =
                Property.builder()
                        .name(propertyDTO.getName())
                        .constructor(propertyDTO.getConstructor())
                        .areaAddr(propertyDTO.getAreaAddr())
                        .modelHouseAddr(propertyDTO.getModelhouseAddr())
                        .phoneNumber(propertyDTO.getPhoneNumber())
                        .contactChannel(propertyDTO.getContactChannel())
                        .homePage(propertyDTO.getHomepage())
                        .startDate(propertyDTO.getStartDate())
                        .endDate(propertyDTO.getEndDate())
                        .propertyType(propertyDTO.getPropertyType())
                        .salesType(propertyDTO.getSalesType())
                        .totalNumber(propertyDTO.getTotalNumber())
                        .companyName(propertyDTO.getCompanyName())
                        .addrDo(propertyDTO.getAddrDo())
                        .addrGu(propertyDTO.getAddrGu())
                        .addrDong(propertyDTO.getAddrDong())
                        .buildingName(propertyDTO.getBuildingName())
                        .admin(admin)
                        .build();

        Property savedProperty = propertyRepository.save(property);

        saveAreas(propertyDTO.getAreas(), savedProperty);
        saveFiles(propertyDTO.getFiles(), savedProperty, admin);
        saveKeywords(propertyDTO.getKeywords(), property);

        return savedProperty;
    }
}
