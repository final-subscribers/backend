package subscribers.clearbunyang.domain.consultation.controller;


import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public class AdminConsultationsController {

    @GetMapping("/api/mock/admin/consultations/{consultingId}")
    public Map<String, Object> getConsultationDetails(
            @PathVariable int consultingId, @RequestParam String status) {

        Map<String, Object> consultationDetails = new HashMap<>();

        if ("pending".equals(status)) {
            consultationDetails.put("name", "이름");
            consultationDetails.put("property_name", "계양 학마을 서원");
            consultationDetails.put("phone_number", "01000001234");
            consultationDetails.put("created_at", "2020-01-01");
            consultationDetails.put("preferred_at", "2020-01-10");
            consultationDetails.put("member_message", "문의");
        } else if ("completed".equals(status)) {
            consultationDetails.put("name", "이현숙");
            consultationDetails.put("phone_number", "01000001234");
            consultationDetails.put("created_at", "2020-01-01");
            consultationDetails.put("completed_at", "2020-02-01");
            consultationDetails.put("member_message", "고객 문의 사항");
            consultationDetails.put("consultant", "a1-2");
            consultationDetails.put("tier", "S");
            consultationDetails.put("consult_message", "상담원 메모");
        } else {
            consultationDetails.put("error", "Invalid status");
        }

        return consultationDetails;
    }
}
