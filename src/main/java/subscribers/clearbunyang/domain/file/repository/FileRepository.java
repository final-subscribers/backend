package subscribers.clearbunyang.domain.file.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import subscribers.clearbunyang.domain.file.entity.File;

public interface FileRepository extends JpaRepository<File, Long> {}
