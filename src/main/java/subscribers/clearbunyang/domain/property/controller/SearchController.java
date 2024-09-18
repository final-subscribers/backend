package subscribers.clearbunyang.domain.property.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subscribers.clearbunyang.domain.property.service.SearchService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/common/properties")
@Tag(name = "검색 화면", description = "매물 검색")
public class SearchController {

    private final SearchService searchService;

    //    @Operation(summary = "검색어 및 검색 필터링 기능을 통해 걸러진 매물 출력")
    //    @GetMapping
    //    public PagedDto<SearchResponse> getSearch(
    //        @RequestParam(required = false) String search,
    //        @RequestParam(required = false) String area,
    //        @RequestParam(required = false) String propertyType,
    //        @RequestParam(required = false) String salesType,
    //        @RequestParam(required = false) String keyword,
    //        @RequestParam(required = false) String price,
    //        @RequestParam(required = false) String squareMeter,
    //        @RequestParam(required = false) String total,
    //        @RequestParam(required = false, value = "size", defaultValue = "9") int size,
    //        @RequestParam(required = false, value = "page", defaultValue = "0") int page
    //    ) {
    //        return searchService.getSearch(search, area, propertyType, salesType, keyword, price,
    // squareMeter, total, size, page);
    //    }
}
