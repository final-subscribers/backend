package subscribers.clearbunyang.domain.property.repository;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import subscribers.clearbunyang.domain.property.entity.Keyword;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    List<Keyword> findByPropertyId(Long propertyId);
}
