package subscribers.clearbunyang.domain.property.service;


import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordType;
import subscribers.clearbunyang.domain.property.entity.enums.PropertyType;
import subscribers.clearbunyang.domain.property.entity.enums.SalesType;
import subscribers.clearbunyang.domain.property.model.response.PropertySearchResponse;
import subscribers.clearbunyang.domain.property.model.response.SearchResponse;
import subscribers.clearbunyang.domain.property.repository.KeywordRepository;
import subscribers.clearbunyang.domain.property.repository.PropertyRepository;
import subscribers.clearbunyang.global.model.PagedDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    private final PropertyRepository propertyRepository;
    private final KeywordRepository keywordRepository;

    public PagedDto<SearchResponse> getSearch(
            String search,
            String area,
            PropertyType propertyType,
            SalesType salesType,
            KeywordType keyword,
            Integer priceMin,
            Integer priceMax,
            Integer areaMin,
            Integer areaMax,
            Integer totalMin,
            Integer totalMax,
            Integer size,
            Integer page) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        String searchParam = (search == null || search.trim().isEmpty()) ? null : search.trim();
        List<String> areaParam =
                (area != null && !area.isEmpty()) ? List.of(area.split("\\s*,\\s*")) : null;

        List<Property> properties =
                propertyRepository.findProperties(
                        searchParam,
                        areaParam,
                        propertyType,
                        salesType,
                        keyword,
                        priceMin,
                        priceMax,
                        areaMin,
                        areaMax,
                        totalMin,
                        totalMax);

        List<PropertySearchResponse> propertySearchResponses =
                properties.stream()
                        .map(
                                property -> {
                                    List<String> infraKeywords =
                                            keywordRepository
                                                    .findByPropertyIdAndTypeAndIsSearchableTrue(
                                                            property.getId(), KeywordType.INFRA)
                                                    .stream()
                                                    .map(keywords -> keywords.getName().name())
                                                    .collect(Collectors.toList());

                                    List<String> benefitKeywords =
                                            keywordRepository
                                                    .findByPropertyIdAndTypeAndIsSearchableTrue(
                                                            property.getId(), KeywordType.BENEFIT)
                                                    .stream()
                                                    .map(keywords -> keywords.getName().name())
                                                    .collect(Collectors.toList());

                                    return PropertySearchResponse.toDto(
                                            property, infraKeywords, benefitKeywords);
                                })
                        .collect(Collectors.toList());

        SearchResponse searchResponse = SearchResponse.toDto(propertySearchResponses);

        return PagedDto.toDTO(page, size, propertySearchResponses.size(), List.of(searchResponse));
    }
}
