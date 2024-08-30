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
import subscribers.clearbunyang.domain.property.model.PropertyDateDto;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;
import subscribers.clearbunyang.global.exception.notFound.EntityNotFoundException;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query(
            "SELECT new subscribers.clearbunyang.domain.property.model.PropertyDateDto(p.id, p.name, p.endDate, p.startDate) "
                    + "FROM Property p WHERE p.startDate <= :today AND p.endDate >= :today ORDER BY p.id DESC")
    List<PropertyDateDto> findPendingPropertiesDateDto(LocalDate today, Pageable pageable);

    default List<PropertyDateDto> getPendingPropertiesDto(LocalDate today, Pageable pageable) {
        List<PropertyDateDto> properties = findPendingPropertiesDateDto(today, pageable);
        return properties != null ? properties : Collections.emptyList();
    }

    @Query(
            "SELECT new subscribers.clearbunyang.domain.property.model.PropertyDateDto(p.id, p.name, p.endDate, p.startDate) "
                    + "FROM Property p WHERE p.endDate < :today ORDER BY p.id DESC")
    List<PropertyDateDto> findCompletedPropertiesDateDto(LocalDate today, Pageable pageable);

    default List<PropertyDateDto> getCompletedPropertiesDto(LocalDate today, Pageable pageable) {
        List<PropertyDateDto> properties = findCompletedPropertiesDateDto(today, pageable);
        return properties != null ? properties : Collections.emptyList();
    }

    default Property getById(Long propertyId) {
        return findById(propertyId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND));
    }

    @Query("SELECT p.id FROM Property p WHERE p.id = :id")
    Optional<Long> findIdById(@Param("id") Long id);

    default Long getIdById(Long propertyId) {
        return findIdById(propertyId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND));
    }
}
