package subscribers.clearbunyang.domain.consultation.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subscribers.clearbunyang.domain.consultation.dto.adminConsultation.request.ConsultRequest;
import subscribers.clearbunyang.domain.consultation.dto.adminConsultation.response.AdminConsultResponse;
import subscribers.clearbunyang.domain.consultation.dto.adminConsultation.response.ConsultCompletedResponse;
import subscribers.clearbunyang.domain.consultation.dto.adminConsultation.response.ConsultPendingResponse;
import subscribers.clearbunyang.domain.consultation.dto.adminConsultation.response.ConsultantListResponse;
import subscribers.clearbunyang.domain.consultation.dto.adminConsultation.response.ConsultantResponse;
import subscribers.clearbunyang.domain.consultation.service.AdminConsultationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/consultations")
@Tag(name = "고객관리- 상담")
public class AdminConsultationController {

    private final AdminConsultationService adminConsultationService;

    @Operation(summary = "상담 완료 조회")
    @GetMapping("/{adminConsultationId}/completed")
    public ConsultCompletedResponse getConsultCompleted(@PathVariable Long adminConsultationId) {
        return adminConsultationService.getConsultCompletedResponse(adminConsultationId);
    }

    @Operation(summary = "상담 대기 조회 ")
    @GetMapping("/{memberConsultationId}/pending")
    public ConsultPendingResponse getConsultPending(@PathVariable Long memberConsultationId) {
        return adminConsultationService.getConsultPendingResponse(memberConsultationId);
    }

    @Operation(summary = "상담사 리스트 조회 ")
    @GetMapping("/{propertyId}")
    public ConsultantListResponse getConsultants(@PathVariable Long propertyId) {
        return adminConsultationService.getConsultants(propertyId);
    }

    @Operation(summary = "상담사 등록")
    @PutMapping("/{adminConsultationId}")
    public ConsultantResponse updateConsultant(
            @PathVariable Long adminConsultationId, @RequestParam String consultant) {
        return adminConsultationService.registerConsultant(adminConsultationId, consultant);
    }

    @Operation(summary = "메모 수정")
    @PutMapping("/{adminConsultationId}/completed")
    public ConsultCompletedResponse updateConsultantMessage(
            @PathVariable Long adminConsultationId, @RequestParam String consultantMessage) {
        return adminConsultationService.updateConsultMessage(
                adminConsultationId, consultantMessage);
    }

    @Operation(summary = "상담 내역 저장")
    @PostMapping("/{memberConsultationId}")
    public AdminConsultResponse createAdminConsult(
            @PathVariable Long memberConsultationId, @RequestBody @Valid ConsultRequest request) {
        return adminConsultationService.createAdminConsult(memberConsultationId, request);
    }
}
