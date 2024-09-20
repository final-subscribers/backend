package subscribers.clearbunyang.domain.dashBoard.service;

import static subscribers.clearbunyang.domain.dashBoard.entity.enums.Phase.*;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import subscribers.clearbunyang.domain.dashBoard.dto.ConsultationDateStatsDTO;
import subscribers.clearbunyang.domain.dashBoard.dto.PropertyGraphRequirementsDTO;
import subscribers.clearbunyang.domain.dashBoard.dto.PropertyInquiryDetailsDTO;
import subscribers.clearbunyang.domain.dashBoard.dto.PropertyInquiryStatusDTO;
import subscribers.clearbunyang.domain.dashBoard.dto.response.CardComponentResponse;
import subscribers.clearbunyang.domain.dashBoard.dto.response.CardCountDescResponse;
import subscribers.clearbunyang.domain.dashBoard.dto.response.CardTodayStatusResponse;
import subscribers.clearbunyang.domain.dashBoard.dto.response.CardWeekProgressResponse;
import subscribers.clearbunyang.domain.dashBoard.dto.response.DropdownSelectsResponse;
import subscribers.clearbunyang.domain.dashBoard.dto.response.GraphRequirementsResponse;
import subscribers.clearbunyang.domain.dashBoard.dto.response.PropertiesInquiryStatusResponse;
import subscribers.clearbunyang.domain.dashBoard.dto.response.PropertyInquiryDetailsResponse;
import subscribers.clearbunyang.domain.dashBoard.dto.response.PropertyInquiryStatusResponse;
import subscribers.clearbunyang.domain.dashBoard.dto.response.PropertySelectResponse;
import subscribers.clearbunyang.domain.dashBoard.entity.enums.GraphInterval;
import subscribers.clearbunyang.domain.dashBoard.repository.DashboardRepository;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final DashboardRepository dashboardRepository;

    public DropdownSelectsResponse getDropdownSelects(Long adminId) {
        List<PropertySelectResponse> openList =
                dashboardRepository.findDropdownSelects(adminId, OPEN).stream()
                        .map(PropertySelectResponse::of)
                        .toList();
        List<PropertySelectResponse> closedList =
                dashboardRepository.findDropdownSelects(adminId, CLOSED).stream()
                        .map(PropertySelectResponse::of)
                        .toList();

        return DropdownSelectsResponse.builder().openList(openList).closedList(closedList).build();
    }

    public CardComponentResponse getCards(Long adminId) {
        CardTodayStatusResponse todayStatus =
                CardTodayStatusResponse.fromDTO(dashboardRepository.findTodayStats(adminId));

        List<ConsultationDateStatsDTO> totalStatsByWeek =
                dashboardRepository.findTotalStatsByWeek(adminId);
        CardWeekProgressResponse thisWeekProgress =
                CardWeekProgressResponse.fromDTO(totalStatsByWeek.get(totalStatsByWeek.size() - 1));
        CardWeekProgressResponse lastWeekProgress =
                CardWeekProgressResponse.fromDTO(totalStatsByWeek.get(totalStatsByWeek.size() - 2));
        List<Integer> totalNumberByWeek = getTotalNumberByWeek(totalStatsByWeek);

        List<PropertyInquiryStatusDTO> properties =
                dashboardRepository.findStatsOrderByCountDesc(adminId);
        CardCountDescResponse highestConsultation =
                CardCountDescResponse.fromDTO(properties.get(0));
        CardCountDescResponse lowestConsultation =
                CardCountDescResponse.fromDTO(properties.get(properties.size() - 1));

        return CardComponentResponse.builder()
                .today(todayStatus)
                .thisWeekProgress(thisWeekProgress)
                .lastWeekProgress(lastWeekProgress)
                .totalNumberByWeek(totalNumberByWeek)
                .highestConsultation(highestConsultation)
                .lowestConsultation(lowestConsultation)
                .build();
    }

    public PropertiesInquiryStatusResponse getPropertiesInquiryStats(
            Long adminId, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<PropertyInquiryStatusResponse> propertiesInquiryStats =
                dashboardRepository
                        .findPropertiesInquiryStats(adminId, pageable)
                        .map(PropertyInquiryStatusResponse::fromDTO);

        return PropertiesInquiryStatusResponse.of(propertiesInquiryStats);
    }

    public PropertyInquiryDetailsResponse getPropertyInquiryDetails(
            Long propertyId, LocalDate date, GraphInterval graphInterval) {

        return dashboardRepository
                .findPropertyInquiryDetails(propertyId, date, graphInterval)
                .map(
                        detailsDTO -> {
                            List<GraphRequirementsResponse> graphRequirementsResponses =
                                    getPropertyGraphRequirements(propertyId, date, graphInterval)
                                            .stream()
                                            .map(GraphRequirementsResponse::of)
                                            .toList();
                            return PropertyInquiryDetailsResponse.of(
                                    detailsDTO, graphRequirementsResponses);
                        })
                .orElseGet(
                        () ->
                                PropertyInquiryDetailsResponse.of(
                                        new PropertyInquiryDetailsDTO(0, 0, 0, 0, 0), null));
    }

    private List<Integer> getTotalNumberByWeek(List<ConsultationDateStatsDTO> totalStatsByWeek) {
        return totalStatsByWeek.stream().map(ConsultationDateStatsDTO::getAll).toList();
    }

    private List<PropertyGraphRequirementsDTO> getPropertyGraphRequirements(
            Long propertyId, LocalDate date, GraphInterval graphInterval) {
        if (graphInterval == GraphInterval.DAILY) {
            return dashboardRepository.findPropertyGraphDaily(propertyId, date);
        } else if (graphInterval == GraphInterval.WEEKLY) {
            return dashboardRepository.findPropertyGraphWeekly(propertyId, date);
        } else if (graphInterval == GraphInterval.MONTHLY) {
            return dashboardRepository.findPropertyGraphMonthly(propertyId, date);
        }
        throw new IllegalArgumentException(ErrorCode.INVALID_INPUT_VALUE.getMessage());
    }
}
