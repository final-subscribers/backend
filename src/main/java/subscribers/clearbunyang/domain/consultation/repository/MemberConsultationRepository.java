package subscribers.clearbunyang.domain.consultation.repository;


import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import subscribers.clearbunyang.domain.consultation.entity.MemberConsultation;
import subscribers.clearbunyang.domain.consultation.entity.enums.Status;
import subscribers.clearbunyang.global.exception.EntityNotFoundException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;

public interface MemberConsultationRepository extends JpaRepository<MemberConsultation, Long> {

    @Query(
            "SELECT COUNT(mc) FROM MemberConsultation mc WHERE mc.member.id = :userId AND mc.status = :status")
    int countConsultationsByUserId(@Param("userId") Long userId, @Param("status") Status status);

    @Query(
            "SELECT mc FROM MemberConsultation mc "
                    + "JOIN mc.property p "
                    + "WHERE mc.member.id = :userId AND mc.status = :status "
                    + "AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) "
                    + "OR LOWER(p.areaAddr) LIKE LOWER(CONCAT('%', :search, '%')) "
                    + "OR LOWER(p.addrDo) LIKE LOWER(CONCAT('%', :search, '%')) "
                    + "OR LOWER(p.addrGu) LIKE LOWER(CONCAT('%', :search, '%')) "
                    + "OR LOWER(p.addrDong) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<MemberConsultation> findConsultationsByUserIdAndSearch(
            @Param("userId") Long userId,
            @Param("search") String search,
            @Param("status") Status status,
            Pageable pageable);

    @Query(
            "SELECT mc FROM MemberConsultation mc "
                    + "LEFT JOIN FETCH mc.adminConsultation ac "
                    + "WHERE mc.id = :id")
    Optional<MemberConsultation> findByIdWithFetchJoin(@Param("id") Long id);

    default MemberConsultation getById(@Param("id") Long id) {
        return findByIdWithFetchJoin(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND));
    }

    // lms 라면 false, 추가 상담 필요 X
    default boolean checkExtraConsultation(String medium) {
        return !"LMS".equals(medium);
    }

    Integer countByPropertyIdAndStatus(Long propertyId, Status status);

    /**
     * propertyId별로 memberCounsultation의 status에 해당하는 개수 리턴
     *
     * @param propertyIds
     * @param status
     * @return
     */
    @Query(
            "SELECT mc.property.id, COUNT(mc) FROM MemberConsultation mc WHERE mc.property.id IN :propertyIds AND mc.status = :status GROUP BY mc.property.id")
    List<Object[]> countPendingByPropertyIds(List<Long> propertyIds, Status status);
}
