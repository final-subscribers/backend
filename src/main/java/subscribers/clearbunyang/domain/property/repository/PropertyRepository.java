package subscribers.clearbunyang.domain.property.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import subscribers.clearbunyang.domain.property.entity.Property;

public interface PropertyRepository extends JpaRepository<Property, Long> {}
