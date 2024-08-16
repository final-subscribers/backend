package subscribers.clearbunyang.domain.consultation.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mock/admin/dashboard")
public class DashboardController {

    @GetMapping
    public Map<String, Object> getDashboard() {

        List<String> statusList = Arrays.asList("계양 학마을서원", "반포 더 숲 자이");
        Map<String, Object> situation = new HashMap<>() {{
            put("name", "계양 학마을서원");
            put("status", "pending"); // 모집 중
            put("period", "monthly");
            put("all", 12); // 상담 신청 총 개수
            put("pending", 9); // 상담 대기 개수
            put("completed", 3); // 상담 완료 개수
            put("phone", "01000001234");
            put("channel", 28);
            put("lms", 10);
            put("each_period", List.of(
                Map.entry("2m", new HashMap<String, Object>() {{
                    put("pending", 13);
                    put("completed", 2);
                }}),
                Map.entry("4m", new HashMap<String, Object>() {{
                    put("pending", 13);
                    put("completed", 2);
                }})
            ));
        }};

        return new HashMap<String, Object>() {{
            put("status_properties", statusList);
            put("situation", situation);
        }};
    }

}
