package subscribers.clearbunyang.domain.consultation.repository;


import io.lettuce.core.dynamic.annotation.Param;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import subscribers.clearbunyang.domain.consultation.entity.MemberConsultation;
import subscribers.clearbunyang.domain.consultation.entity.enums.Status;
import subscribers.clearbunyang.domain.consultation.exception.ConsultationException;
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
            throw new ConsultationException(ErrorCode.PHONE_NUMBER_DUPLICATION);
        }
    }

    @Query(
            value =
                    "SELECT mc FROM MemberConsultation mc "
                            + "LEFT JOIN FETCH mc.adminConsultation ac "
                            + "WHERE mc.property.id = :propertyId "
                            + "AND mc.status = :status "
                            + "AND (:name IS NULL OR :phoneNumber IS NULL OR "
                            + "    (mc.memberName LIKE %:name% AND mc.phoneNumber LIKE %:phoneNumber%) OR "
                            + "    (mc.memberName LIKE %:phoneNumber% AND mc.phoneNumber LIKE %:name%)) "
                            + "AND (:consultant IS NULL OR ac.consultant LIKE %:consultant%) "
                            + "AND (:preferredAt IS NULL OR mc.preferredAt = :preferredAt) "
                            + "ORDER BY mc.createdAt DESC")
    Page<MemberConsultation> findByPropertyIdAndFilters(
            @Param("propertyId") Long propertyId,
            @Param("status") Status status,
            @Param("name") String name,
            @Param("phoneNumber") String phoneNumber,
            @Param("preferredAt") LocalDate preferredAt,
            @Param("consultant") String consultant,
            Pageable pageable);

    // lms 라면 false, 추가 상담 필요 X
    @Query("SELECT CASE " + "WHEN :medium = 'LMS' THEN false " + "ELSE true " + "END")
    boolean checkExtraConsultation(@Param("medium") String medium);

    /*
        // 여기서 adminConsultation 추가하면 쿼리 증가. property left join 시 한 줄 감소
        @Query("SELECT mc FROM MemberConsultation mc "
                + "LEFT JOIN FETCH mc.property p "
                + "WHERE mc.id = :id AND mc.status = :status")
        Optional<MemberConsultation> findByIdAndStatus(Long id, Status status);
    */
}
