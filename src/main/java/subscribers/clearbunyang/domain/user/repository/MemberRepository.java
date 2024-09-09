package subscribers.clearbunyang.domain.user.repository;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import subscribers.clearbunyang.domain.user.entity.Member;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;
import subscribers.clearbunyang.global.exception.notFound.EntityNotFoundException;

// 어드민측이 일반유저 정보조회
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    default Member findMemberById(Long memberId) {
        return findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND));
    }
}
