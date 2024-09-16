package subscribers.clearbunyang.domain.property.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordName;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordType;
import subscribers.clearbunyang.domain.property.model.request.KeywordRequestDTO;
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

    @Enumerated(EnumType.STRING)
    private KeywordName name;

    @Enumerated(EnumType.STRING)
    private KeywordType type; // 혜택/인프라

    @Column(nullable = false)
    private boolean isSearchable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    @JsonIgnore
    private Property property;

    public static Keyword toEntity(
            KeywordRequestDTO keywordRequestDTO, String jsonValue, Property property) {
        return Keyword.builder()
                .name(keywordRequestDTO.getName())
                .type(keywordRequestDTO.getName().getType())
                .jsonValue(jsonValue)
                .isSearchable(keywordRequestDTO.getSearchEnabled())
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
