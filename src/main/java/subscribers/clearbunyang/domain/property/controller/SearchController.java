package subscribers.clearbunyang.domain.property.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordType;
import subscribers.clearbunyang.domain.property.entity.enums.PropertyType;
import subscribers.clearbunyang.domain.property.entity.enums.SalesType;
import subscribers.clearbunyang.domain.property.model.response.SearchResponse;
import subscribers.clearbunyang.domain.property.service.SearchService;
import subscribers.clearbunyang.global.model.PagedDto;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/common/properties")
@Tag(name = "검색 화면", description = "매물 검색")
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "검색어 및 검색 필터링 기능을 통해 걸러진 매물 출력")
    @GetMapping
    public PagedDto<SearchResponse> getSearch(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String area,
            @RequestParam(required = false) PropertyType propertyType,
            @RequestParam(required = false) SalesType salesType,
            @RequestParam(required = false) KeywordType keyword,
            @RequestParam(required = false) Integer priceMin,
            @RequestParam(required = false) Integer priceMax,
            @RequestParam(required = false) Integer areaMin,
            @RequestParam(required = false) Integer areaMax,
            @RequestParam(required = false) Integer totalMin,
            @RequestParam(required = false) Integer totalMax,
            @RequestParam(required = false, value = "size", defaultValue = "9") int size,
            @RequestParam(required = false, value = "page", defaultValue = "0") int page) {
        return searchService.getSearch(
                search,
                area,
                propertyType,
                salesType,
                keyword,
                priceMin,
                priceMax,
                areaMin,
                areaMax,
                totalMin,
                totalMax,
                size,
                page);
    }
}
