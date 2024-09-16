package subscribers.clearbunyang.domain.property.repository;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import subscribers.clearbunyang.domain.property.entity.Keyword;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    List<Keyword> findByPropertyId(Long propertyId);

    @Modifying
    @Query("DELETE FROM Keyword k where k.property.id=:propertyId")
    int deleteByPropertyId(Long propertyId);
}
