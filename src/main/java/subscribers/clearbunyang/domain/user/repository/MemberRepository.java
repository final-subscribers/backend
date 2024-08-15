package subscribers.clearbunyang.domain.user.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import subscribers.clearbunyang.domain.user.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {}
