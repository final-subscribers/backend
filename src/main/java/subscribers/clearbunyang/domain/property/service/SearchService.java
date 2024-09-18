package subscribers.clearbunyang.domain.property.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import subscribers.clearbunyang.domain.property.repository.PropertyRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    private final PropertyRepository propertyRepository;
    //
    //    public PagedDto<SearchResponse> getSearch(
    //        String search, String area, String propertyType, String salesType, String keyword,
    // String price, String squareMeter, String total, int size, int page
    //    ) {
    //
    //    }
}
