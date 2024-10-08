package subscribers.clearbunyang.domain.consultation.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subscribers.clearbunyang.domain.consultation.dto.adminPropertyConsultation.request.NewCustomerAdditionRequest;
import subscribers.clearbunyang.domain.consultation.dto.adminPropertyConsultation.response.ConsultCompletedListResponse;
import subscribers.clearbunyang.domain.consultation.dto.adminPropertyConsultation.response.ConsultPendingListResponse;
import subscribers.clearbunyang.domain.consultation.dto.adminPropertyConsultation.response.SideBarSelectedPropertyResponse;
import subscribers.clearbunyang.domain.consultation.entity.enums.Tier;
import subscribers.clearbunyang.domain.consultation.service.AdminPropertyConsultationService;
import subscribers.clearbunyang.global.dto.PagedDtoWithTotalCount;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/properties/{propertyId}/consultations")
@Tag(name = "고객관리- 매물에 대한 상담")
public class AdminPropertyConsultationController {

    private final AdminPropertyConsultationService adminPropertyConsultationService;

    @Operation(summary = "신규 고객 상담 등록")
    @PostMapping
    public void createNewCustomerAddition(
            @PathVariable(required = false) Long propertyId,
            @RequestBody @Valid NewCustomerAdditionRequest request) {

        adminPropertyConsultationService.createNewCustomerAddition(propertyId, request);
    }

    @Operation(summary = "사이드바에 있는 매물 조회")
    @GetMapping("/sidebar")
    public SideBarSelectedPropertyResponse getSideBarSelectedProperty(
            @PathVariable Long propertyId) {

        return adminPropertyConsultationService.getSideBarSelectedProperty(propertyId);
    }

    @Operation(summary = "상담 대기 리스트 조회")
    @GetMapping("/pending")
    public PagedDtoWithTotalCount<ConsultPendingListResponse> getConsultPendingList(
            @PathVariable Long propertyId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String consultant,
            @RequestParam(required = false, name = "preferredAt") LocalDate preferredAt,
            @RequestParam(required = false, value = "page", defaultValue = "0") int page,
            @RequestParam(required = false, value = "size", defaultValue = "5") int size) {

        PagedDtoWithTotalCount<ConsultPendingListResponse> response =
                adminPropertyConsultationService.getConsultPendingListResponse(
                        propertyId, search, consultant, preferredAt, page, size);

        return response;
    }

    @Operation(summary = "상담 완료 리스트 조회")
    @GetMapping("/completed")
    public PagedDtoWithTotalCount<ConsultCompletedListResponse> getConsultCompletedList(
            @PathVariable(required = false) Long propertyId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Tier tier,
            @RequestParam(required = false) String consultant,
            @RequestParam(required = false, name = "preferredAt") LocalDate preferredAt,
            @RequestParam(required = false, value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {

        PagedDtoWithTotalCount<ConsultCompletedListResponse> response =
                adminPropertyConsultationService.getConsultCompletedListResponse(
                        propertyId, search, tier, consultant, preferredAt, page, size);

        return response;
    }
}
