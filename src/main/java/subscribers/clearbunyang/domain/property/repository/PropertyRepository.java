package subscribers.clearbunyang.domain.property.repository;


import io.lettuce.core.dynamic.annotation.Param;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;
import subscribers.clearbunyang.global.exception.notFound.EntityNotFoundException;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query(
            "SELECT p FROM Property p WHERE p.startDate <= :today AND p.endDate >= :today ORDER BY p.id DESC")
    List<Property> findPendingProperties(LocalDate today, Pageable pageable);

    default List<Property> getPendingProperties(LocalDate today, Pageable pageable) {
        List<Property> properties = findPendingProperties(today, pageable);
        return properties != null ? properties : Collections.emptyList();
    }

    @Query("SELECT p FROM Property p WHERE p.endDate < :today ORDER BY p.id DESC")
    List<Property> findCompletedProperties(LocalDate today, Pageable pageable);

    default List<Property> getCompletedProperties(LocalDate today, Pageable pageable) {
        List<Property> properties = findCompletedProperties(today, pageable);
        return properties != null ? properties : Collections.emptyList();
    }

    @Query("SELECT p FROM Property p LEFT JOIN FETCH p.admin a WHERE p.id = :id")
    Optional<Property> findByIdWithFetchJoin(@Param("id") Long id);

    default Property getById(Long propertyId) {
        return findByIdWithFetchJoin(propertyId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND));
    }

    @Query("SELECT p.id FROM Property p WHERE p.id = :id")
    Optional<Long> findIdById(@Param("id") Long id);

    default Long getIdById(Long propertyId) {
        return findIdById(propertyId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND));
    }
}
