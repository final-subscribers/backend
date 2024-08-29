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

    /**
     * 키워드들을 저장하는 메소드
     *
     * @param keywordDTOS
     * @param property
     */
    public void saveKeywords(List<KeywordDTO> keywordDTOS, Property property) {
        List<Keyword> searchableKeywords = new ArrayList<>();
        List<KeywordDTO> nonSearchableKeywords = new ArrayList<>();

        for (KeywordDTO keywordDTO : keywordDTOS) {
            if (keywordDTO.getSearchEnabled()) {
                Keyword keyword = Keyword.toEntity(keywordDTO, objectToJson(keywordDTO), property);
                searchableKeywords.add(keyword);
            } else {
                nonSearchableKeywords.add(keywordDTO);
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
            List<KeywordDTO> nonSearchableKeywords, Property property) {
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
     * @param areaDTOs
     * @param property
     */
    private void saveAreas(List<AreaDTO> areaDTOs, Property property) {
        List<Area> areas =
                areaDTOs.stream()
                        .map(areaDTO -> Area.toEntity(areaDTO, property))
                        .collect(Collectors.toList());

        areaRepository.saveAll(areas);
    }

    /**
     * 파일을 저장하는 메소드
     *
     * @param fileDTOs
     * @param property
     * @param admin
     */
    private void saveFiles(List<FileDTO> fileDTOs, Property property, Admin admin) {
        List<File> files =
                fileDTOs.stream()
                        .map(fileDTO -> File.toEntity(fileDTO, property, admin))
                        .collect(Collectors.toList());

        fileRepository.saveAll(files);
    }

    /**
     * 물건을 저장하는 메소드
     *
     * @param propertyDTO
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
}
