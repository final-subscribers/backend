package subscribers.clearbunyang.domain.consultation.repository.dashboard;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import subscribers.clearbunyang.domain.consultation.model.dashboard.ConsultationProgressDTO;

public interface DashboardRepository {
    Page<ConsultationProgressDTO> findConsultationProgress(Long userId, Pageable pageable);
}
