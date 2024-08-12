package subscribers.clearbunyang.domain.user.repository;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import subscribers.clearbunyang.domain.user.entity.Member;

// 어드민측이 일반유저 정보조회
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);
}
