package subscribers.clearbunyang.domain.property.repository;


import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import subscribers.clearbunyang.domain.property.entity.Property;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query(
            "SELECT p FROM Property p WHERE "
                    + "(:isOpen = true AND p.startDate <= :currentDate AND p.endDate >= :currentDate) "
                    + "OR (:isOpen = false AND p.endDate < :currentDate)")
    Page<Property> findByDateRange(
            @Param("currentDate") LocalDate currentDate,
            @Param("pageable") Pageable pageable,
            @Param("isOpen") boolean isOpen);
}
