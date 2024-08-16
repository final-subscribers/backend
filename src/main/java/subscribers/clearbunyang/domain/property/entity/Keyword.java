package subscribers.clearbunyang.domain.property.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import subscribers.clearbunyang.domain.property.entity.enums.Name;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordRank;
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

    @Enumerated(EnumType.STRING)
    private KeywordRank keywordRank;

    @Enumerated(EnumType.STRING)
    private Name name;

    @Column(nullable = false)
    private String value;

    @Enumerated(EnumType.STRING)
    private Type type;
}
