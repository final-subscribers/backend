package subscribers.clearbunyang.domain.property.repository;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import subscribers.clearbunyang.domain.property.entity.Area;

public interface AreaRepository extends JpaRepository<Area, Long> {
    List<Area> findByPropertyId(Long propertyId);
}
