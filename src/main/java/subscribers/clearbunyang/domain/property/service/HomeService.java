package subscribers.clearbunyang.domain.property.service;


import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import subscribers.clearbunyang.domain.likes.repository.LikesRepository;
import subscribers.clearbunyang.domain.property.dto.response.HomeResponse;
import subscribers.clearbunyang.domain.property.dto.response.PropertySummaryResponse;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordType;
import subscribers.clearbunyang.domain.property.repository.KeywordRepository;
import subscribers.clearbunyang.domain.property.repository.PropertyRepository;
import subscribers.clearbunyang.global.dto.PagedDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeService {

    private final PropertyRepository propertyRepository;
    private final LikesRepository likesRepository;
    private final KeywordRepository keywordRepository;

    final int size = 5;
    final int totalItems = 20;

    //    @Transactional(readOnly = true)
    public PagedDto<HomeResponse> getHome(Long memberId, int page) {

        List<Property> top20Properties = propertyRepository.findTop20ByOrderByLikeCountDesc();

        int start = page * size;
        int end = Math.min(start + size, totalItems);
        List<Property> pagedProperties = top20Properties.subList(start, end);

        List<PropertySummaryResponse> propertiesResponse =
                pagedProperties.stream()
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
                                                    .map(keyword -> keyword.getName().name())
                                                    .collect(Collectors.toList());

                                    List<String> benefitKeywords =
                                            keywordRepository
                                                    .findByPropertyIdAndTypeAndIsSearchableTrue(
                                                            property.getId(), KeywordType.BENEFIT)
                                                    .stream()
                                                    .map(keyword -> keyword.getName().name())
                                                    .collect(Collectors.toList());

                                    return PropertySummaryResponse.toDto(
                                            property, infraKeywords, benefitKeywords, likesExisted);
                                })
                        .collect(Collectors.toList());

        HomeResponse homeResponse = HomeResponse.toDto(propertiesResponse);

        return PagedDto.toDTO(page, size, totalItems / size, List.of(homeResponse));
    }
}
