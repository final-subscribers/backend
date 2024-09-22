package subscribers.clearbunyang.domain.property.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subscribers.clearbunyang.domain.property.dto.response.SearchResponse;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordName;
import subscribers.clearbunyang.domain.property.entity.enums.PropertyType;
import subscribers.clearbunyang.domain.property.entity.enums.SalesType;
import subscribers.clearbunyang.domain.property.service.SearchService;
import subscribers.clearbunyang.global.dto.PagedDto;
import subscribers.clearbunyang.global.security.details.CustomUserDetails;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Tag(name = "검색 화면", description = "매물 검색")
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "비로그인/검색 기능을 통해 걸러진 매물 출력")
    @GetMapping("/common/properties")
    public PagedDto<SearchResponse> getCommonPropertyBySearching(
            @RequestParam(required = false) String search,
            @RequestParam(required = false, value = "size", defaultValue = "9") int size,
            @RequestParam(required = false, value = "page", defaultValue = "0") int page) {

        return searchService.getPropertyBySearching(null, search, size, page);
    }

    @Operation(summary = "로그인/검색 기능을 통해 걸러진 매물 출력")
    @GetMapping("/member/properties")
    public PagedDto<SearchResponse> getMemberPropertyBySearching(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, value = "size", defaultValue = "9") int size,
            @RequestParam(required = false, value = "page", defaultValue = "0") int page) {
        Long memberId = customUserDetails.getUserId();

        return searchService.getPropertyBySearching(memberId, search, size, page);
    }

    @Operation(summary = "비로그인/필터링 기능을 통해 걸러진 매물 출력")
    @GetMapping("/common/properties/filter")
    public PagedDto<SearchResponse> getCommonPropertyByFiltering(
            @RequestParam(required = false) String area,
            @RequestParam(required = false) List<PropertyType> propertyType,
            @RequestParam(required = false) List<SalesType> salesType,
            @RequestParam(required = false) List<KeywordName> keyword,
            @RequestParam(required = false) Integer priceMin,
            @RequestParam(required = false) Integer priceMax,
            @RequestParam(required = false) Integer areaMin,
            @RequestParam(required = false) Integer areaMax,
            @RequestParam(required = false) Integer totalMin,
            @RequestParam(required = false) Integer totalMax,
            @RequestParam(required = false, value = "size", defaultValue = "9") int size,
            @RequestParam(required = false, value = "page", defaultValue = "0") int page) {

        return searchService.getPropertyByFiltering(
                null,
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

    @Operation(summary = "로그인/필터링 기능을 통해 걸러진 매물 출력")
    @GetMapping("/member/properties/filter")
    public PagedDto<SearchResponse> getMemberPropertyByFiltering(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(required = false) String area,
            @RequestParam(required = false) List<PropertyType> propertyType,
            @RequestParam(required = false) List<SalesType> salesType,
            @RequestParam(required = false) List<KeywordName> keyword,
            @RequestParam(required = false) Integer priceMin,
            @RequestParam(required = false) Integer priceMax,
            @RequestParam(required = false) Integer areaMin,
            @RequestParam(required = false) Integer areaMax,
            @RequestParam(required = false) Integer totalMin,
            @RequestParam(required = false) Integer totalMax,
            @RequestParam(required = false, value = "size", defaultValue = "9") int size,
            @RequestParam(required = false, value = "page", defaultValue = "0") int page) {
        Long memberId = customUserDetails.getUserId();

        return searchService.getPropertyByFiltering(
                memberId,
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
