package subscribers.clearbunyang.domain.consultation.repository;


import io.lettuce.core.dynamic.annotation.Param;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import subscribers.clearbunyang.domain.consultation.entity.MemberConsultation;
import subscribers.clearbunyang.global.exception.Invalid.InvalidValueException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;
import subscribers.clearbunyang.global.exception.notFound.EntityNotFoundException;

public interface MemberConsultationRepository extends JpaRepository<MemberConsultation, Long> {

    @Query(
            "SELECT mc FROM MemberConsultation mc "
                    + "LEFT JOIN FETCH mc.adminConsultation ac "
                    + "WHERE mc.id = :id")
    Optional<MemberConsultation> findByIdWithFetchJoin(@Param("id") Long id);

    default MemberConsultation getById(@Param("id") Long id) {
        return findByIdWithFetchJoin(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND));
    }

    boolean existsByPhoneNumber(String phoneNumber);

    default void checkPhoneNumberExists(String phoneNumber) {
        if (existsByPhoneNumber(phoneNumber)) {
            throw new InvalidValueException(ErrorCode.PHONE_NUMBER_DUPLICATION);
        }
    }

    // lms 라면 false, 추가 상담 필요 X
    default boolean checkExtraConsultation(String medium) {
        return !"LMS".equals(medium);
    }
}
