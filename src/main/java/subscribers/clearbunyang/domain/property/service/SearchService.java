package subscribers.clearbunyang.domain.property.service;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import subscribers.clearbunyang.domain.likes.repository.LikesRepository;
import subscribers.clearbunyang.domain.property.dto.response.PropertySummaryResponse;
import subscribers.clearbunyang.domain.property.dto.response.SearchResponse;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordType;
import subscribers.clearbunyang.domain.property.entity.enums.PropertyType;
import subscribers.clearbunyang.domain.property.entity.enums.SalesType;
import subscribers.clearbunyang.domain.property.repository.KeywordRepository;
import subscribers.clearbunyang.domain.property.repository.PropertyRepository;
import subscribers.clearbunyang.global.dto.PagedDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    private final PropertyRepository propertyRepository;
    private final KeywordRepository keywordRepository;
    private final LikesRepository likesRepository;

    public PagedDto<SearchResponse> getPropertyBySearching(
            Long memberId, String search, Integer size, Integer page) {

        String searchParam = (search == null || search.trim().isEmpty()) ? "" : search.trim();

        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Property> propertyPage =
                propertyRepository.findPropertiesBySearching(searchParam, pageRequest);

        List<PropertySummaryResponse> propertySearchResponses =
                propertyPage.stream()
                        .map(
                                property -> {
                                    boolean likesExisted = false;
                                    if (memberId != null) {
                                        likesExisted =
                                                likesRepository.existsByMemberIdAndPropertyId(
                                                        memberId, property.getId());
                                    }

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

                                    return PropertySummaryResponse.toDto(
                                            property, infraKeywords, benefitKeywords, likesExisted);
                                })
                        .collect(Collectors.toList());

        SearchResponse searchResponse = SearchResponse.toDto(propertySearchResponses);

        return PagedDto.toDTO(page, size, propertyPage.getTotalPages(), List.of(searchResponse));
    }

    public PagedDto<SearchResponse> getPropertyByFiltering(
            Long memberId,
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

        List<String> areaParam =
                (area != null && !area.isEmpty())
                        ? Arrays.stream(area.split("/"))
                                .map(String::trim) // 각 지역 이름에 공백이 있다면 제거
                                .collect(Collectors.toList())
                        : null;

        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Property> propertyPage =
                propertyRepository.findPropertiesByFiltering(
                        areaParam,
                        propertyType,
                        salesType,
                        keyword,
                        priceMin,
                        priceMax,
                        areaMin,
                        areaMax,
                        totalMin,
                        totalMax,
                        pageRequest);

        List<PropertySummaryResponse> propertySearchResponses =
                propertyPage.stream()
                        .map(
                                property -> {
                                    boolean likesExisted = false;
                                    if (memberId != null) {
                                        likesExisted =
                                                likesRepository.existsByMemberIdAndPropertyId(
                                                        memberId, property.getId());
                                    }

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

                                    return PropertySummaryResponse.toDto(
                                            property, infraKeywords, benefitKeywords, likesExisted);
                                })
                        .collect(Collectors.toList());

        SearchResponse searchResponse = SearchResponse.toDto(propertySearchResponses);

        return PagedDto.toDTO(page, size, propertyPage.getTotalPages(), List.of(searchResponse));
    }
}
