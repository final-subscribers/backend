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
import subscribers.clearbunyang.domain.property.model.KeywordDTO;
import subscribers.clearbunyang.global.entity.BaseEntity;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "keyword")
public class Keyword extends BaseEntity {

    @Column(columnDefinition = "JSON", nullable = false)
    private String jsonValue;

    private String name;

    private String type; // 혜택/인프라

    @Column(nullable = false)
    private boolean isSearchable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    public static Keyword toEntity(KeywordDTO keywordDTO, String jsonValue, Property property) {
        return Keyword.builder()
                .name(keywordDTO.getName())
                .type(keywordDTO.getType())
                .jsonValue(jsonValue)
                .isSearchable(keywordDTO.getSearchEnabled())
                .property(property)
                .build();
    }

    public static Keyword toEntity(String jsonValue, Property property) {
        return Keyword.builder()
                .jsonValue(jsonValue)
                .isSearchable(false)
                .property(property)
                .build();
    }
}
