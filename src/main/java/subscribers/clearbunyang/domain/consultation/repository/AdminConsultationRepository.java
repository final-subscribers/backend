package subscribers.clearbunyang.domain.consultation.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import subscribers.clearbunyang.domain.consultation.entity.AdminConsultation;

public interface AdminConsultationRepository extends JpaRepository<AdminConsultation, Long> {}
