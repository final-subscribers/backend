package subscribers.clearbunyang.domain.consultation.repository.dashboard;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import subscribers.clearbunyang.domain.consultation.model.dashboard.ConsultationProgressDTO;
import subscribers.clearbunyang.domain.user.entity.Admin;

public interface DashboardRepository {
    Page<ConsultationProgressDTO> findConsultationProgress(Admin admin, Pageable pageable);
}
