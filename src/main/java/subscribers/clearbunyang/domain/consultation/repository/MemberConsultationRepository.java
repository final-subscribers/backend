package subscribers.clearbunyang.domain.consultation.repository;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import subscribers.clearbunyang.domain.consultation.entity.MemberConsultation;

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
}
