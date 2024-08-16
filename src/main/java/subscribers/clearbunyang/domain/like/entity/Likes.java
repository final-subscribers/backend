package subscribers.clearbunyang.domain.like.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.user.entity.Member;
import subscribers.clearbunyang.global.entity.BaseEntity;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "likes")
public class Likes extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;
}
