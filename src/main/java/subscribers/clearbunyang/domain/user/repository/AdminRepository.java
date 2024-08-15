package subscribers.clearbunyang.domain.user.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import subscribers.clearbunyang.domain.user.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {}
