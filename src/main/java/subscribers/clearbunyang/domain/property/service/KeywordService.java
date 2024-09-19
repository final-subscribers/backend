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
import subscribers.clearbunyang.domain.property.dto.request.KeywordRequest;
import subscribers.clearbunyang.domain.property.dto.response.KeywordResponse;
import subscribers.clearbunyang.domain.property.entity.Keyword;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordType;
import subscribers.clearbunyang.domain.property.exception.JsonConversionException;
import subscribers.clearbunyang.domain.property.repository.KeywordRepository;

@RequiredArgsConstructor
@Service
public class KeywordService {
    private final KeywordRepository keywordRepository;
    private final ObjectMapper objectMapper;

    /**
     * 키워드들을 저장하는 메소드
     *
     * @param keywordRequests
     * @param property
     */
    @Transactional
    public void saveKeywords(List<KeywordRequest> keywordRequests, Property property) {
        List<Keyword> searchableKeywords = new ArrayList<>();
        List<KeywordRequest> nonSearchableKeywords = new ArrayList<>();

        for (KeywordRequest keywordRequest : keywordRequests) {
            if (keywordRequest.getSearchEnabled()) {
                Keyword keyword =
                        Keyword.toEntity(keywordRequest, objectToJson(keywordRequest), property);
                searchableKeywords.add(keyword);
            } else {
                nonSearchableKeywords.add(keywordRequest);
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
    @Transactional
    public void saveNonSearchableKeywordsAsJson(
            List<KeywordRequest> nonSearchableKeywords, Property property) {
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
    @Transactional(readOnly = true)
    public Map<KeywordType, List<KeywordResponse>> categorizedKeywords(Long propertyId) {
        List<Keyword> keywords = keywordRepository.findByPropertyId(propertyId);
        try {
            List<KeywordResponse> keywordList = new ArrayList<>();
            for (Keyword keyword : keywords) {

                if (keyword.isSearchable()) { // 단일 객체를 읽어드림
                    keywordList.add(
                            objectMapper.readValue(keyword.getJsonValue(), KeywordResponse.class));
                } else { // 리스트로 읽어 들임
                    List<KeywordResponse> keywordRequestDTOS =
                            objectMapper.readValue(
                                    keyword.getJsonValue(),
                                    new TypeReference<List<KeywordResponse>>() {});
                    keywordList.addAll(keywordRequestDTOS);
                }
            }

            return keywordList.stream().collect(Collectors.groupingBy(KeywordResponse::getType));

        } catch (Exception e) {
            throw new JsonConversionException();
        }
    }
}
