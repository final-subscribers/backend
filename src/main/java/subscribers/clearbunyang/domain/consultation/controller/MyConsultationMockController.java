package subscribers.clearbunyang.domain.consultation.controller;


import java.util.ArrayList;
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
@RequestMapping("api/member/my-consultations")
@RequiredArgsConstructor
public class MyConsultationMockController {

    // 상담 대기, 완료 모두 동일함
    @GetMapping("/pending")
    public Map<String, Object> getMyPendingConsultations(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) {
        int offset = (page - 1) * size;

        // 상담 내역
        List<Map<String, Object>> consultations = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            HashMap<String, Object> consultation = new HashMap<>();
            consultation.put(
                    "image_url",
                    "https://search.pstatic.net/common/?src=http%3A%2F%2Fcafefiles.naver.net%2FMjAxODA0MjhfMjYz%2FMDAxNTI0OTAyMDI3ODAz.MFDCwAmFB91efs0LS0Bm07NjmuUOJDkEh-h3nvC9kJ0g.s9XRo7n9wR1Pp4hZUP0avxIl-Ct9W0FCBKU07-iC-nAg.PNG.skywnb%2FexternalFile.png&type=sc960_832");
            consultation.put("id", i + 1);
            consultation.put("name", "잠실 프루지오" + (i + 1));
            consultation.put("consultation_created_at", "2024-07-20");
            consultation.put("message", "상담 신청합니다!");
            consultation.put("member_name", "박지선");
            consultation.put("phone_number", "010-1234-5678");
            consultation.put("preferred_at", "2024-07-21");
            consultations.add(consultation);
        }

        // 페이지 처리
        List<Map<String, Object>> pagedData =
                consultations.stream().skip(offset).limit(size).collect(Collectors.toList());

        // 응답 데이터 구성
        Map<String, Object> response = new HashMap<>();
        response.put("count", consultations.size()); // 총 상담 신청수
        response.put(
                "totalPages", (int) Math.ceil((double) consultations.size() / size)); // 전체 페이지 수
        response.put("pageSize", size); // 페이지 크기
        response.put("currentPage", page); // 현재 페이지
        response.put("consultations", pagedData); // 현재 페이지의 상담 내역

        return response;
    }

    @GetMapping("/completed")
    public Map<String, Object> getMyCompletedConsultations(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) {
        int offset = (page - 1) * size;

        // 상담 내역
        List<Map<String, Object>> consultations = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            HashMap<String, Object> consultation = new HashMap<>();
            consultation.put(
                    "image_url",
                    "https://search.pstatic.net/common/?src=http%3A%2F%2Fcafefiles.naver.net%2FMjAxODA0MjhfMjYz%2FMDAxNTI0OTAyMDI3ODAz.MFDCwAmFB91efs0LS0Bm07NjmuUOJDkEh-h3nvC9kJ0g.s9XRo7n9wR1Pp4hZUP0avxIl-Ct9W0FCBKU07-iC-nAg.PNG.skywnb%2FexternalFile.png&type=sc960_832");
            consultation.put("id", i + 1);
            consultation.put("name", "잠실 프루지오" + (i + 1));
            consultation.put("consultation_created_at", "2024-07-20");
            consultation.put("message", "상담 신청합니다!");
            consultation.put("member_name", "박지선");
            consultation.put("phone_number", "010-1234-5678");
            consultation.put("preferred_at", "2024-07-21");
            consultations.add(consultation);
        }

        // 페이지 처리
        List<Map<String, Object>> pagedData =
                consultations.stream().skip(offset).limit(size).collect(Collectors.toList());

        // 응답 데이터 구성
        Map<String, Object> response = new HashMap<>();
        response.put("count", consultations.size()); // 총 상담 신청수
        response.put(
                "totalPages", (int) Math.ceil((double) consultations.size() / size)); // 전체 페이지 수
        response.put("pageSize", size); // 페이지 크기
        response.put("currentPage", page); // 현재 페이지
        response.put("consultations", pagedData); // 현재 페이지의 상담 내역

        return response;
    }
}
