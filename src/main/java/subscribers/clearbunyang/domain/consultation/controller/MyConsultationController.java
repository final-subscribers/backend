package subscribers.clearbunyang.domain.consultation.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subscribers.clearbunyang.domain.consultation.model.myConsultations.ConsultationPagedResponse;
import subscribers.clearbunyang.domain.consultation.service.MyConsultationService;
import subscribers.clearbunyang.global.model.PagedDto;
import subscribers.clearbunyang.global.security.details.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member/my-consultations")
@Tag(name = "My Consultations", description = "내 상담 신청 내역")
public class MyConsultationController {

    private final MyConsultationService myConsultationService;

    @Operation(summary = "대기중인 상태")
    @GetMapping("/pending")
    public PagedDto<ConsultationPagedResponse> getMyPendingConsultationsList(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, value = "page", defaultValue = "0") int page,
            @RequestParam(required = false, value = "size", defaultValue = "5") int size) {
        Long userId = user.getUserId();
        return myConsultationService.getMyPendingConsultationsList(userId, search, page, size);
    }

    @Operation(summary = "상담 완료 상태")
    @GetMapping("/completed")
    public ConsultationPagedResponse getMyCompletedConsultationsList(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, value = "page", defaultValue = "0") int page,
            @RequestParam(required = false, value = "size", defaultValue = "5") int size) {
        Long userId = user.getUserId();
        return myConsultationService.getMyCompletedConsultationsList(userId, search, page, size);
    }
}
