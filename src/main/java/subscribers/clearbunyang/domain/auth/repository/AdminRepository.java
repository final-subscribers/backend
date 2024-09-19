package subscribers.clearbunyang.domain.auth.repository;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import subscribers.clearbunyang.domain.auth.entity.Admin;
import subscribers.clearbunyang.global.exception.EntityNotFoundException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;

// 회사측에서 어드민 정보 및 파일 조회
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(String email);

    boolean existsByEmail(String email);

    default Admin findAdminById(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
