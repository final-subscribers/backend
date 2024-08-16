package subscribers.clearbunyang.domain.property.controller;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mock/admin/properties/{productId}/consultation")
class AdminPropertiesMockController {

    // http://localhost:8080/api/mock/admin/properties/1/consultation/sidebar
    @GetMapping("/sidebar")
    public Map<String, Object> getSidebar(@PathVariable Long productId) {
        // 사이드바
        Map<String, List<Map<String, Object>>> SideBarListResponse = new HashMap<>();

        // 모집중 데이터
        List<Map<String, Object>> pendingList = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            Map<String, Object> pending = new HashMap<>();
            pending.put("id", i);
            pending.put("name", "김포 북변 우미린");
            pendingList.add(pending);
        }

        // 모집완 데이터
        List<Map<String, Object>> completedList = new ArrayList<>();
        for (int i = 0; i <= 20; i++) {
            Map<String, Object> completed = new HashMap<>();
            completed.put("id", i);
            completed.put("name", "리버사이드");
            completedList.add(completed);
        }
        SideBarListResponse.put("Pending", pendingList);
        SideBarListResponse.put("Completed", completedList);

        // 현재 선택된 매물 정보
        Map<String, Object> selectedProperty = new HashMap<>();
        selectedProperty.put("id", 1L);
        selectedProperty.put("name", "김포 북변 우미린 파크리브");
        selectedProperty.put("image", "imgeUrl");
        selectedProperty.put("property_type", "apartment");
        selectedProperty.put("company_name", "(주)선우");
        selectedProperty.put("constructor", "효성중공");
        selectedProperty.put("total_number", 345);
        selectedProperty.put("start_date", "2020-01-01");
        selectedProperty.put("end_date", "2020-08-08");

        // 최종 전체 데이터
        Map<String, Object> sidebar = new HashMap<>();
        sidebar.put("SideBarListResponse", SideBarListResponse);
        sidebar.put("selected_property", selectedProperty);

        return sidebar;
    }

    // - 상담신청_상담대기_ 검색_상담신청일자, 고객명, 전화번호
    // http://localhost:8080/api/mock/admin/properties/1/consultation/pending?search=search&page=1&preferred_at=2020-01-10&size=5
    @GetMapping("/pending")
    public Map<String, Object> getPendingConsultation(
            @PathVariable Long productId,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "preferred_at", required = false) LocalDate preferredAt) {

        int offset = (page - 1) * size;
        List<Map<String, Object>> consultPendingSummary = new ArrayList<>();

        // 예시 데이터 생성
        for (int i = 1; i <= 20; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", "search");
            item.put("phone_number", "01000001234");
            item.put("created_at", "2020-01-10");
            item.put("preferred_at", "2020-01-10");
            item.put("consultant", "a1-999");
            consultPendingSummary.add(item);
        }
        // 페이지 처리
        List<Map<String, Object>> pagedData =
                consultPendingSummary.stream()
                        .skip(offset)
                        .limit(size)
                        .collect(Collectors.toList());

        // 응답 데이터 구성
        Map<String, Object> response = new HashMap<>();
        response.put("ConsultPendingSummary", pagedData);
        response.put("totalPages", (int) Math.ceil((double) consultPendingSummary.size() / size));
        response.put("pageSize", size);
        response.put("currentPage", page);

        Map<String, Object> result = new HashMap<>();
        result.put("ConsultPendingResponse", response);

        return result;
    }

    // 상담 신청_ 상담 완료 검색
    // 고객명, 전화번호, 고객 등급, 상담사, 상담 신청 일자 검색
    // http://localhost:8080/api/mock/admin/properties/1/consultation/completed?search=boreum&page=1&preferred_at=2020-01-10&size=5&rank=A&consultant=
    @GetMapping("/completed")
    public Map<String, Object> getCompletedConsultation(
            @PathVariable Long productId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String rank,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) LocalDate preferredAt,
            @RequestParam(required = false) String consultant) {

        int offset = (page - 1) * size;
        List<Map<String, Object>> ConsultCompletedSummary = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", "boreum");
            item.put("rank", "A");
            item.put("preferred_at", "2020-01-10");
            item.put("completed_at", "2020-01-10");
            item.put("phone_number", "01000001234");
            item.put("consultant", "a1-1");
            ConsultCompletedSummary.add(item);
        }
        // 검색 기능 확인 위한 데이터
        Map<String, Object> searchItem = new HashMap<>();
        searchItem.put("name", "search");
        searchItem.put("rank", "S");
        searchItem.put("phone_number", "01012345678");
        searchItem.put("preferred_at", "2020-01-11");
        searchItem.put("consultant", "a1-999");
        ConsultCompletedSummary.add(searchItem);

        // Pagination
        List<Map<String, Object>> pagedData = new ArrayList<>();
        for (int i = offset; i < offset + size && i < ConsultCompletedSummary.size(); i++) {
            pagedData.add(ConsultCompletedSummary.get(i));
        }

        // Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("ConsultCompletedSummary", pagedData);
        response.put("currentPage", page);
        response.put("pageSize", size);
        response.put("totalPages", (int) Math.ceil((double) ConsultCompletedSummary.size() / size));

        Map<String, Object> result = new HashMap<>();
        result.put("ConsultCompletedResponse", response);

        return result;
    }
}
