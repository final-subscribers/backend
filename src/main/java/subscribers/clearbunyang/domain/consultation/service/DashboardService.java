package subscribers.clearbunyang.domain.consultation.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import subscribers.clearbunyang.domain.consultation.model.dashboard.ConsultationProgressDTO;
import subscribers.clearbunyang.domain.consultation.repository.dashboard.DashboardRepository;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final DashboardRepository dashboardRepository;

    public Page<ConsultationProgressDTO> getConsultationProgress(Long userId, Pageable pageable) {
        return dashboardRepository.findConsultationProgress(userId, pageable);
    }
}
