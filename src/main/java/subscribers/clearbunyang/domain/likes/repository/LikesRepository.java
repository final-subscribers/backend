package subscribers.clearbunyang.domain.likes.repository;


import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import subscribers.clearbunyang.domain.auth.entity.Member;
import subscribers.clearbunyang.domain.likes.entity.Likes;
import subscribers.clearbunyang.domain.property.entity.Property;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    Optional<Likes> findByMemberAndProperty(Member member, Property property);

    boolean existsByMemberIdAndPropertyId(Long memberId, Long propertyId);

    @Modifying
    @Query("DELETE FROM Likes li where li.property.id=:propertyId")
    int deleteByPropertyId(Long propertyId);

    List<Likes> findByPropertyId(Long propertyId);
}
