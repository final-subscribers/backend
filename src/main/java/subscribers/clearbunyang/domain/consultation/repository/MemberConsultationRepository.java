package subscribers.clearbunyang.domain.consultation.repository;


import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import subscribers.clearbunyang.domain.consultation.entity.MemberConsultation;
import subscribers.clearbunyang.global.exception.Invalid.InvalidValueException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;
import subscribers.clearbunyang.global.exception.notFound.EntityNotFoundException;

public interface MemberConsultationRepository extends JpaRepository<MemberConsultation, Long> {

    @Query("SELECT COUNT(mc) FROM MemberConsultation mc WHERE mc.member.id = :userId")
    int countConsultationsByUserId(@Param("userId") Long userId);

    @Query(
            "SELECT mc FROM MemberConsultation mc "
                    + "JOIN mc.property p "
                    + "WHERE mc.member.id = :userId AND mc.adminConsultation IS NULL "
                    + "AND (LOWER(p.buildingName) LIKE LOWER(CONCAT('%', :search, '%')) "
                    + "OR LOWER(p.addrDo) LIKE LOWER(CONCAT('%', :search, '%')) "
                    + "OR LOWER(p.addrGu) LIKE LOWER(CONCAT('%', :search, '%')) "
                    + "OR LOWER(p.addrDong) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<MemberConsultation> findPendingConsultationsByUserIdAndSearch(
            @Param("userId") Long userId, @Param("search") String search);

    @Query(
            "SELECT mc FROM MemberConsultation mc "
                    + "JOIN mc.property p "
                    + "WHERE mc.member.id = :userId AND mc.adminConsultation IS NOT NULL "
                    + "AND (LOWER(p.buildingName) LIKE LOWER(CONCAT('%', :search, '%')) "
                    + "OR LOWER(p.addrDo) LIKE LOWER(CONCAT('%', :search, '%')) "
                    + "OR LOWER(p.addrGu) LIKE LOWER(CONCAT('%', :search, '%')) "
                    + "OR LOWER(p.addrDong) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<MemberConsultation> findCompletedConsultationsByUserIdAndSearch(
            @Param("userId") Long userId, @Param("search") String search);

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
