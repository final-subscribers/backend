package subscribers.clearbunyang.global.file.repository;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import subscribers.clearbunyang.global.file.entity.File;

public interface FileRepository extends JpaRepository<File, Long> {
    @Modifying
    @Query("DELETE FROM File f where f.property.id=:propertyId")
    int deleteByPropertyId(Long propertyId);

    List<File> findByPropertyId(Long propertyId);
}
