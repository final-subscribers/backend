package subscribers.clearbunyang.domain.like.repository;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import subscribers.clearbunyang.domain.likes.entity.Likes;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.user.entity.Member;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    Optional<Likes> findByMemberAndProperty(Member member, Property property);

    boolean existsByMemberIdAndPropertyId(Long memberId, Long propertyId);
}
