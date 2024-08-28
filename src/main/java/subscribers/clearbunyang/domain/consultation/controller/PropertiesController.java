package subscribers.clearbunyang.domain.consultation.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import subscribers.clearbunyang.domain.consultation.entity.MemberConsultation;
import subscribers.clearbunyang.domain.consultation.entity.enums.Tier;
import subscribers.clearbunyang.domain.consultation.model.request.NewCustomerAdditionRequest;
import subscribers.clearbunyang.domain.consultation.model.response.ConsultCompletedListResponse;
import subscribers.clearbunyang.domain.consultation.model.response.ConsultPendingListResponse;
import subscribers.clearbunyang.domain.consultation.model.response.PagedDTO;
import subscribers.clearbunyang.domain.consultation.model.response.SideBarListResponse;
import subscribers.clearbunyang.domain.consultation.service.PropertiesService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/properties/{propertyId}/consultations")
public class PropertiesController {

    private final PropertiesService propertiesService;

    @Operation(summary = "신규 고객 등록", description = "어드민이 신규 고객을 등록합니다")
    @ApiResponse(
            description = "신규 고객 등록 성공",
            content =
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MemberConsultation.class)))
    @PostMapping
    public MemberConsultation createNewCustomerAddition(
            @PathVariable(required = false) Long propertyId,
            @RequestBody @Valid NewCustomerAdditionRequest request) {

        return propertiesService.createNewCustomerAddition(propertyId, request);
    }

    @Operation(summary = "사이드바", description = "사이드바를 조회합니다")
    @ApiResponse(
            description = "사이드바 조회 성공",
            content =
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SideBarListResponse.class)))
    @GetMapping("/sidebar")
    public SideBarListResponse getSidebar(@PathVariable Long propertyId) {

        return propertiesService.getSideBarList(propertyId);
    }

    @Operation(summary = "상담 대기 리스트", description = "상담 대기 리스트를 조회합니다")
    @ApiResponse(
            description = "상담 대기 리스트 조회 성공",
            content =
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConsultPendingListResponse.class)))
    @GetMapping("/pending")
    public PagedDTO<ConsultPendingListResponse> getConsultPendingList(
            @PathVariable Long propertyId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String consultant,
            @RequestParam(required = false, name = "preferred_at") LocalDate preferredAt,
            @RequestParam(required = false, value = "page", defaultValue = "0") int page,
            @RequestParam(required = false, value = "size", defaultValue = "5") int size) {

        PagedDTO<ConsultPendingListResponse> response =
                propertiesService.getConsultPendingListResponse(
                        propertyId, search, consultant, preferredAt, page, size);

        return response;
    }

    @Operation(summary = "상담 완료 리스트", description = "상담 완료 리스트를 조회합니다")
    @ApiResponse(
            description = "상담 완료 리스트 조회 성공",
            content =
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConsultCompletedListResponse.class)))
    @GetMapping("/completed")
    public PagedDTO<ConsultCompletedListResponse> getConsultCompletedList(
            @PathVariable(required = false) Long propertyId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Tier tier,
            @RequestParam(required = false) String consultant,
            @RequestParam(required = false, name = "preferred_at") LocalDate preferredAt,
            @RequestParam(required = false, value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {

        PagedDTO<ConsultCompletedListResponse> response =
                propertiesService.getConsultCompletedListResponse(
                        propertyId, search, tier, consultant, preferredAt, page, size);

        return response;
    }
}
