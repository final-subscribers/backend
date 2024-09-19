package subscribers.clearbunyang.domain.consultation.repository;


import io.lettuce.core.dynamic.annotation.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import subscribers.clearbunyang.domain.consultation.entity.AdminConsultation;
import subscribers.clearbunyang.domain.consultation.entity.enums.Status;
import subscribers.clearbunyang.domain.consultation.entity.enums.Tier;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;
import subscribers.clearbunyang.global.exception.notFound.EntityNotFoundException;

public interface AdminConsultationRepository extends JpaRepository<AdminConsultation, Long> {

    @Query(
            "SELECT ac FROM AdminConsultation ac "
                    + "LEFT JOIN FETCH ac.memberConsultation mc "
                    + "WHERE ac.id = :id")
    Optional<AdminConsultation> findByIdWithFetchJoin(@Param("id") Long id);

    default AdminConsultation getById(Long id) {
        return findByIdWithFetchJoin(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND));
    }

    @Query(
            "SELECT ac FROM AdminConsultation ac LEFT JOIN FETCH ac.memberConsultation mc "
                    + "WHERE mc.property.id = :propertyId ")
    List<AdminConsultation> findByPropertyId(Long propertyId);

    @Query(
            value =
                    "SELECT ac FROM AdminConsultation ac "
                            + "LEFT JOIN FETCH ac.memberConsultation mc "
                            + "WHERE mc.property.id = :propertyId "
                            + "AND mc.status = :status "
                            + "AND (:name IS NULL OR :phoneNumber IS NULL OR "
                            + "    (mc.memberName LIKE %:name% AND mc.phoneNumber LIKE %:phoneNumber%) OR "
                            + "    (mc.memberName LIKE %:phoneNumber% AND mc.phoneNumber LIKE %:name%)) "
                            + "AND (:tier IS NULL OR ac.tier = :tier) "
                            + "AND (:consultant IS NULL OR ac.consultant LIKE %:consultant%) "
                            + "AND (:preferredAt IS NULL OR mc.preferredAt = :preferredAt) "
                            + "ORDER BY ac.id DESC")
    Page<AdminConsultation> findByPropertyIdAndCompletedAndFilters(
            @Param("propertyId") Long propertyId,
            @Param("status") Status status,
            @Param("name") String name,
            @Param("phoneNumber") String phoneNumber,
            @Param("tier") Tier tier,
            @Param("consultant") String consultant,
            @Param("preferredAt") LocalDate preferredAt,
            Pageable pageable);

    @Query(
            value =
                    "SELECT ac FROM AdminConsultation ac "
                            + "LEFT JOIN FETCH ac.memberConsultation mc "
                            + "WHERE mc.property.id = :propertyId "
                            + "AND mc.status = :status "
                            + "AND (:name IS NULL OR :phoneNumber IS NULL OR "
                            + "    (mc.memberName LIKE %:name% AND mc.phoneNumber LIKE %:phoneNumber%) OR "
                            + "    (mc.memberName LIKE %:phoneNumber% AND mc.phoneNumber LIKE %:name%)) "
                            + "AND (:consultant IS NULL OR ac.consultant LIKE %:consultant%) "
                            + "AND (:preferredAt IS NULL OR mc.preferredAt = :preferredAt) "
                            + "ORDER BY ac.id DESC")
    Page<AdminConsultation> findByPropertyIdAndPendingAndFilters(
            @Param("propertyId") Long propertyId,
            @Param("status") Status status,
            @Param("name") String name,
            @Param("phoneNumber") String phoneNumber,
            @Param("preferredAt") LocalDate preferredAt,
            @Param("consultant") String consultant,
            Pageable pageable);
}
