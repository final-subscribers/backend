package subscribers.clearbunyang.domain.property.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import subscribers.clearbunyang.domain.property.entity.Keyword;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordType;
import subscribers.clearbunyang.domain.property.exception.JsonConversionException;
import subscribers.clearbunyang.domain.property.model.request.KeywordRequestDTO;
import subscribers.clearbunyang.domain.property.model.response.KeywordResponseDTO;
import subscribers.clearbunyang.domain.property.repository.KeywordRepository;

@RequiredArgsConstructor
@Service
public class KeywordService {
    private final KeywordRepository keywordRepository;
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
}
