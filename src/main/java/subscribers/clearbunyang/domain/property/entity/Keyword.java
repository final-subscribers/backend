package subscribers.clearbunyang.domain.property.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import subscribers.clearbunyang.domain.property.entity.enums.Name;
import subscribers.clearbunyang.domain.property.entity.enums.Rank;
import subscribers.clearbunyang.domain.property.entity.enums.Type;
import subscribers.clearbunyang.global.entity.BaseEntity;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "keyword")
public class Keyword extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Column(nullable = false)
    private Rank rank;

    @Column(nullable = false)
    private Name name;

    @Column(nullable = false)
    private String value;

    @Column(nullable = false)
    private Type type;
}
