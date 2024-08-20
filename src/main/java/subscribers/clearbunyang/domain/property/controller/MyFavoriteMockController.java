package subscribers.clearbunyang.domain.property.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/member/my-favorites")
@RequiredArgsConstructor
public class MyFavoriteMockController {

    @GetMapping
    public Map<String, Object> getMyFavorites(
            @RequestParam(name = "status") String status,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "12") int size) {

        int offset = (page - 1) * size;
        List<Map<String, Object>> properties = new ArrayList<>();

        List<List<String>> keywords =
                Arrays.asList(
                        Arrays.asList("할인분양", "혜택"),
                        Arrays.asList("지하철", "편의시설"),
                        Arrays.asList("버스", "편의시설"));

        for (int i = 0; i < 20; i++) {
            HashMap<String, Object> property = new HashMap<>();
            property.put("id", i + 1);
            property.put("keyword", keywords);
            property.put("name", "잠실 프루지오" + (i + 1));
            property.put("area_addr", "서울특별시 서초구 ");
            property.put("created_at", "2024-07-20");
            property.put("property_type", "아파트");
            property.put("sales_type", "민간 분양");
            property.put("count", 100);
            property.put("price", 29000);
            property.put("sales_price", 27000);
            properties.add(property);
        }

        List<Map<String, Object>> pagedData =
                properties.stream().skip(offset).limit(size).collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("favorite_number", 20);
        response.put("totalPages", (int) Math.ceil((double) properties.size() / size));
        response.put("pageSize", size);
        response.put("currentPage", page);
        response.put("properties", pagedData);

        return response;
    }
}
