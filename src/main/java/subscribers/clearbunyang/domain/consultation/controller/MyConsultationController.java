/*
package subscribers.clearbunyang.domain.consultation.controller;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member/my-consultations")
@Tag(name = "My Consultations", description = "내 상담 신청 내역")
public class MyConsultationController {

    private final MyConsultationService myConsultationService;

    @Operation(summary = "대기중인 상태")
    @GetMapping("/pending")
    public ConsultationPagedResponse getMyPendingConsultationsList(
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
*/
