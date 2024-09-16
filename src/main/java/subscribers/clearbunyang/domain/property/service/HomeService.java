package subscribers.clearbunyang.domain.property.service;


import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.domain.likes.repository.LikesRepository;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.model.response.HomePropertiesResponse;
import subscribers.clearbunyang.domain.property.model.response.HomeResponse;
import subscribers.clearbunyang.domain.property.repository.PropertyRepository;
import subscribers.clearbunyang.global.model.PagedDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeService {

    private final PropertyRepository propertyRepository;
    private final LikesRepository likesRepository;
    private final PropertyService propertyService;

    final int size = 5;
    final int totalSize = 4;

    @Transactional(readOnly = true)
    public PagedDto<HomeResponse> getHome(Long memberId, int page) {

        List<Property> top20Properties = propertyRepository.findTop20ByOrderByLikeCountDesc();

        List<HomePropertiesResponse> propertiesResponse =
                top20Properties.stream()
                        .map(
                                property -> {
                                    boolean likesExisted = false;
                                    if (memberId != null) {
                                        likesExisted =
                                                likesRepository.existsByMemberIdAndPropertyId(
                                                        memberId, property.getId());
                                    }

                                    return HomePropertiesResponse.toDto(property, likesExisted);
                                })
                        .collect(Collectors.toList());

        HomeResponse homeResponse = HomeResponse.toDto(propertiesResponse);

        return PagedDto.toDTO(page, size, totalSize, List.of(homeResponse));
    }
}
