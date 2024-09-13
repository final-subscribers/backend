package subscribers.clearbunyang.domain.property.service;


import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import subscribers.clearbunyang.domain.consultation.model.myConsultations.MyPendingConsultationsResponse;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.model.response.HomePagedResponse;
import subscribers.clearbunyang.domain.property.model.response.HomePropertiesResponse;
import subscribers.clearbunyang.domain.property.repository.PropertyRepository;
import subscribers.clearbunyang.global.model.PagedDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeService {

    private final PropertyRepository propertyRepository;

    public HomePagedResponse getHome(int page) {

        List<Property> top20Properties = propertyRepository.findTop20ByOrderByLikeCountDesc();

        List<HomePropertiesResponse> propertiesResponse =
                top20Properties.stream()
                        .map(HomePropertiesResponse::toDto)
                        .collect(Collectors.toList());

        PagedDto<List<HomePropertiesResponse>> pagedDto =
                PagedDto.<List<MyPendingConsultationsResponse>>builder()
                        .totalPages(4)
                        .pageSize(5)
                        .currentPage(page)
                        .content(propertiesResponse)
                        .build();
    }
}
