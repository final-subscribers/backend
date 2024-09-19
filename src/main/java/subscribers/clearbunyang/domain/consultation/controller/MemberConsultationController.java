package subscribers.clearbunyang.domain.consultation.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subscribers.clearbunyang.domain.consultation.dto.memberConsultations.ConsultationResponse;
import subscribers.clearbunyang.domain.consultation.service.MemberConsultationService;
import subscribers.clearbunyang.global.dto.PagedDto;
import subscribers.clearbunyang.global.security.details.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member/my-consultations")
@Tag(name = "내 상담 신청 내역 조회", description = "member가 등록한 상담 신청 내역 조회")
public class MemberConsultationController {

    private final MemberConsultationService memberConsultationService;

    @Operation(summary = "대기중인 상태")
    @GetMapping("/pending")
    public PagedDto<ConsultationResponse> getMyPendingConsultationsList(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, value = "page", defaultValue = "0") int page,
            @RequestParam(required = false, value = "size", defaultValue = "5") int size) {
        Long userId = user.getUserId();
        return memberConsultationService.getMyPendingConsultationsList(userId, search, page, size);
    }

    @Operation(summary = "상담 완료 상태")
    @GetMapping("/completed")
    public PagedDto<ConsultationResponse> getMyCompletedConsultationsList(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, value = "page", defaultValue = "0") int page,
            @RequestParam(required = false, value = "size", defaultValue = "5") int size) {
        Long userId = user.getUserId();
        return memberConsultationService.getMyCompletedConsultationsList(
                userId, search, page, size);
    }
}
