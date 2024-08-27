package subscribers.clearbunyang.domain.consultation.controller;

import static subscribers.clearbunyang.domain.consultation.entity.enums.Period.*;
import static subscribers.clearbunyang.domain.consultation.entity.enums.Status.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subscribers.clearbunyang.domain.consultation.entity.enums.Period;
import subscribers.clearbunyang.domain.consultation.entity.enums.Status;
import subscribers.clearbunyang.domain.consultation.model.dashboard.ConsultationProgressDTO;
import subscribers.clearbunyang.domain.consultation.model.dashboard.DashboardPropertyDTO;
import subscribers.clearbunyang.domain.consultation.model.dashboard.DashboardResponseDTO;

@RestController
@RequestMapping("/mock/admin/dashboard")
public class DashboardController {

    @GetMapping
    public DashboardResponseDTO getDashboard() {
        return DashboardResponseDTO.builder()
            .today(
                ConsultationProgressDTO.builder()
                    .all(12)
                    .pending(9)
                    .completed(3)
                    .build()
            )
            .thisWeek(
                ConsultationProgressDTO.builder()
                    .all(12)
                    .pending(9)
                    .completed(3)
                    .build()
            )
            .lastWeek(
                ConsultationProgressDTO.builder()
                    .all(12)
                    .pending(9)
                    .completed(3)
                    .build()
            )
            .lastFiveWeeks(
                List.of(
                    ConsultationProgressDTO.builder()
                        .periodLabel("1st")
                        .all(12)
                        .build(),
                    ConsultationProgressDTO.builder()
                        .periodLabel("2nd")
                        .all(3)
                        .build(),
                    ConsultationProgressDTO.builder()
                        .periodLabel("3rd")
                        .all(13)
                        .build(),
                    ConsultationProgressDTO.builder()
                        .periodLabel("4th")
                        .all(12)
                        .build(),
                    ConsultationProgressDTO.builder()
                        .periodLabel("5th")
                        .all(14)
                        .build()
                )
            )
            .highestConsultation(
                ConsultationProgressDTO.builder()
                    .propertyName("청계 힐스테이트")
                    .all(72)
                    .build()
            )
            .lowestConsultation(
                ConsultationProgressDTO.builder()
                    .propertyName("양평 리버사이드")
                    .all(3)
                    .build()
            )
            .allProperties(15)
            .properties(
                List.of(
                    ConsultationProgressDTO.builder()
                        .propertyName("반포 더 숲 자이")
                        .pending(9)
                        .completed(3)
                        .build(),
                    ConsultationProgressDTO.builder()
                        .propertyName("반포 더 바다 자이")
                        .pending(9)
                        .completed(3)
                        .build()
                )
            )
            .statusProperties(
                List.of(
                    "계양 학마을서원", "반포 더 숲 자이", "청계 힐스테이트", "양평 리버사이드"
                )
            )
            .situation(
                DashboardPropertyDTO.builder()
                    .propertyName("계양 학마을서원")
                    .status(PENDING)
                    .period(MONTHLY)
                    .completed(9)
                    .phone(23)
                    .channel(28)
                    .lms(10)
                    .eachPeriod(
                        List.of(
                            ConsultationProgressDTO.builder()
                                .periodLabel("2m")
                                .pending(13)
                                .completed(2)
                                .build(),
                            ConsultationProgressDTO.builder()
                                .periodLabel("4m")
                                .pending(13)
                                .completed(2)
                                .build()
                        )
                    )
                    .build()
            )
            .build();
    }

    @GetMapping("/properties")
    public DashboardResponseDTO getDashboardProperties(
        @RequestParam(name = "page", defaultValue = "1") int page,
        @RequestParam(name = "size", defaultValue = "5") int size
    ) {
        return DashboardResponseDTO.builder()
            .allProperties(15)
            .properties(
                List.of(
                    ConsultationProgressDTO.builder()
                        .propertyName("반포 더 숲 자이")
                        .pending(9)
                        .completed(3)
                        .build(),
                    ConsultationProgressDTO.builder()
                        .propertyName("반포 더 바다 자이")
                        .pending(9)
                        .completed(3)
                        .build()
                )
            )
            .build();
    }

    @GetMapping("/properties/{property_id}")
    public DashboardPropertyDTO getDashboardProperty(
        @PathVariable(name = "property_id") String propertyId
    ) {
        return DashboardPropertyDTO.builder()
            .propertyName("계양 학마을서원")
            .status(PENDING)
            .period(MONTHLY)
            .completed(9)
            .phone(23)
            .channel(28)
            .lms(10)
            .eachPeriod(
                List.of(
                    ConsultationProgressDTO.builder()
                        .periodLabel("2m")
                        .pending(13)
                        .completed(2)
                        .build(),
                    ConsultationProgressDTO.builder()
                        .periodLabel("4m")
                        .pending(13)
                        .completed(2)
                        .build()
                )
            )
            .build();
    }

}
