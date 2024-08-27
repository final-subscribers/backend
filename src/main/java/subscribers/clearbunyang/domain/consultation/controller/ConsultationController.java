package subscribers.clearbunyang.domain.consultation.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import subscribers.clearbunyang.domain.consultation.model.response.ConsultCompletedListResponse;
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

    @Operation(summary = "상담 완료 모달창", description = "상담 완료 모달창을 조회합니다")
    @ApiResponse(
            description = "상담 완료 모달창을 조회 성공",
            content =
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConsultCompletedResponse.class)))
    @GetMapping("/{adminConsultationId}/completed") // admin
    public ConsultCompletedResponse getConsultCompleted(@PathVariable Long adminConsultationId) {
        return consultationService.getConsultCompletedResponse(adminConsultationId);
    }

    @Operation(summary = "상담 대기 모달창", description = "상담 대기 모달창을 조회합니다")
    @ApiResponse(
            description = "상담 대기 모달창을 조회 성공",
            content =
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConsultPendingResponse.class)))
    @GetMapping("/{memberConsultationId}/pending") // membcreateAdminConsulter
    public ConsultPendingResponse getConsultPending(@PathVariable Long memberConsultationId) {
        return consultationService.getConsultPendingResponse(memberConsultationId);
    }

    @Operation(summary = "상담 대기 상담사 리스트 조회", description = "상담 대기 상담사 조회 drop down")
    @ApiResponse(
            description = "상담 대기 상담사 리스트 조회 완료",
            content =
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConsultCompletedListResponse.class)))
    @GetMapping("/{propertyId}")
    public ConsultantListResponse getConsultants(@PathVariable Long propertyId) {
        return consultationService.getConsultants(propertyId);
    }

    @Operation(summary = "상담 대기 상담사 변경", description = "상담 대기 상담사 변경 drop down")
    @ApiResponse(
            description = "상담 대기 상담사 변경 완료",
            content =
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConsultantResponse.class)))
    @PutMapping("/{adminConsultationId}")
    public ConsultantResponse updateConsultant(
            @PathVariable Long adminConsultationId, @RequestParam String consultant) {
        return consultationService.changeConsultant(adminConsultationId, consultant);
    }

    @Operation(summary = "상담 완료 모달 상담 메모 수정", description = "상담 완료 모달 상담 메모를 수정합니다")
    @ApiResponse(
            description = "상담 완료 모달 상담 메모를 수정 완료",
            content =
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConsultCompletedResponse.class)))
    @PutMapping("/{adminConsultationId}/completed")
    public ConsultCompletedResponse updateConsultantMessage(
            @PathVariable Long adminConsultationId, @RequestParam String consultantMessage) {
        return consultationService.updateConsultMessage(adminConsultationId, consultantMessage);
    }

    @Operation(summary = "상담 답변", description = "상담에 답변합니다")
    @ApiResponse(
            description = "상담 완료 모달 상담 메모를 수정 완료",
            content =
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AdminConsultResponse.class)))
    @PostMapping("/{memberConsultationId}")
    public AdminConsultResponse createAdminConsult(
            @PathVariable Long memberConsultationId, @RequestBody ConsultRequest request) {
        return consultationService.createAdminConsult(memberConsultationId, request);
    }
}
