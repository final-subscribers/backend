package subscribers.clearbunyang.domain.property.repository;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import subscribers.clearbunyang.domain.property.entity.Area;

public interface AreaRepository extends JpaRepository<Area, Long> {
    List<Area> findByPropertyId(Long propertyId);

    @Modifying
    @Query("DELETE FROM Area a where a.property.id=:propertyId")
    int deleteByPropertyId(Long propertyId);
}
