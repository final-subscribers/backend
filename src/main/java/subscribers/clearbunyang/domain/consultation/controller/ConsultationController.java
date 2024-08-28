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
import subscribers.clearbunyang.domain.consultation.model.request.ConsultRequest;
import subscribers.clearbunyang.domain.consultation.model.response.AdminConsultResponse;
import subscribers.clearbunyang.domain.consultation.model.response.ConsultCompletedResponse;
import subscribers.clearbunyang.domain.consultation.model.response.ConsultPendingResponse;
import subscribers.clearbunyang.domain.consultation.model.response.ConsultantListResponse;
import subscribers.clearbunyang.domain.consultation.model.response.ConsultantResponse;
import subscribers.clearbunyang.domain.consultation.service.ConsultationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/consultations")
public class ConsultationController {

    private final ConsultationService consultationService;

    @Tag(name = "상담 완료", description = "상담 완료 단건 조회")
    @Operation(summary = "상담 완료 조회")
    @GetMapping("/{adminConsultationId}/completed")
    public ConsultCompletedResponse getConsultCompleted(@PathVariable Long adminConsultationId) {
        return consultationService.getConsultCompletedResponse(adminConsultationId);
    }

    @Tag(name = "상담 대기 모달", description = "상담 대기 단건 조회")
    @Operation(summary = "상담 대기 조회 ")
    @GetMapping("/{memberConsultationId}/pending")
    public ConsultPendingResponse getConsultPending(@PathVariable Long memberConsultationId) {
        return consultationService.getConsultPendingResponse(memberConsultationId);
    }

    @Tag(name = "상담사 조회", description = "상담 대기 상담사 조회")
    @Operation(summary = "상담사 리스트 조회 ")
    @GetMapping("/{propertyId}")
    public ConsultantListResponse getConsultants(@PathVariable Long propertyId) {
        return consultationService.getConsultants(propertyId);
    }

    @Tag(name = "상담사 변경", description = "상담 대기 상담사 변경")
    @Operation(summary = "상담사 변경")
    @PutMapping("/{adminConsultationId}")
    public ConsultantResponse updateConsultant(
            @PathVariable Long adminConsultationId, @RequestParam String consultant) {
        return consultationService.changeConsultant(adminConsultationId, consultant);
    }

    @Tag(name = "상담사 메모 수정", description = "상담 완료 메모 수정")
    @Operation(summary = "메모 수정")
    @PutMapping("/{adminConsultationId}/completed")
    public ConsultCompletedResponse updateConsultantMessage(
            @PathVariable Long adminConsultationId, @RequestParam String consultantMessage) {
        return consultationService.updateConsultMessage(adminConsultationId, consultantMessage);
    }

    @Tag(name = "상담 답변", description = "상담 답변")
    @Operation(summary = "상담 답변 ")
    @PostMapping("/{memberConsultationId}")
    public AdminConsultResponse createAdminConsult(
            @PathVariable Long memberConsultationId, @RequestBody @Valid ConsultRequest request) {
        return consultationService.createAdminConsult(memberConsultationId, request);
    }
}
