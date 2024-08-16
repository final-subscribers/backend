package subscribers.clearbunyang.domain.consultation.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import subscribers.clearbunyang.domain.consultation.entity.MemberConsultation;

public interface MemberConsultationRepository extends JpaRepository<MemberConsultation, Long> {}
