package subscribers.clearbunyang.domain.consultation.controller;


import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mock/admin/consultations/1")
public class MockConsultationController {

    // 고객 관리 - 상담 완료 - 내역보기 모달
    @GetMapping("/completed")
    public Map<String, Object> getCompletedConsultation() {
        Map<String, Object> response = new HashMap<>();
        response.put("name", "고객 이름");
        response.put("phoneNumber", "01043613333");
        response.put("createdAt", "2024-08-22");
        response.put("completedAt", "2024-08-22");
        response.put("memberMessage", "상담 신청합니다");
        response.put("tier", "A");
        response.put("consultMessage", "상담을 진행함");
        return response;
    }

    // 고객 관리 - 상담 대기 - 내역보기 모달
    @GetMapping("/pending")
    public Map<String, Object> getPendingConsultation() {
        Map<String, Object> response = new HashMap<>();
        response.put("memberName", "고객 이름");
        response.put("propertyName", "매물 이름");
        response.put("phoneNumber", "01012341234");
        response.put("createdAt", "2024-08-08");
        response.put("preferredAt", "2024-08-25");
        response.put("consultMessage", "추가 상담을 요함");
        response.put("memberMessage", "상담 문의 합니다");
        response.put("addConsultation", "true");
        return response;
    }
}
