package subscribers.clearbunyang.domain.property.repository;


import io.lettuce.core.dynamic.annotation.Param;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.domain.property.dto.PropertyDateDto;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordName;
import subscribers.clearbunyang.domain.property.entity.enums.PropertyType;
import subscribers.clearbunyang.domain.property.entity.enums.SalesType;
import subscribers.clearbunyang.global.exception.EntityNotFoundException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query(
            "SELECT new subscribers.clearbunyang.domain.property.dto.PropertyDateDto(p.id, p.name, p.endDate, p.startDate) "
                    + "FROM Property p WHERE p.startDate <= :today AND p.endDate >= :today ORDER BY p.id DESC")
    List<PropertyDateDto> findPendingPropertiesDateDto(LocalDate today, Pageable pageable);

    default List<PropertyDateDto> getPendingPropertiesDto(LocalDate today, Pageable pageable) {
        List<PropertyDateDto> properties = findPendingPropertiesDateDto(today, pageable);
        return properties != null ? properties : Collections.emptyList();
    }

    @Query(
            "SELECT new subscribers.clearbunyang.domain.property.dto.PropertyDateDto(p.id, p.name, p.endDate, p.startDate) "
                    + "FROM Property p WHERE p.endDate < :today ORDER BY p.id DESC")
    List<PropertyDateDto> findCompletedPropertiesDateDto(LocalDate today, Pageable pageable);

    default List<PropertyDateDto> getCompletedPropertiesDto(LocalDate today, Pageable pageable) {
        List<PropertyDateDto> properties = findCompletedPropertiesDateDto(today, pageable);
        return properties != null ? properties : Collections.emptyList();
    }

    default Property getById(Long propertyId) {
        return findById(propertyId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND));
    }

    @Query("SELECT p.id FROM Property p WHERE p.id = :id")
    Optional<Long> findIdById(@Param("id") Long id);

    default Long getIdById(Long propertyId) {
        return findIdById(propertyId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND));
    }

    default Property findPropertyById(Long propertyId) {
        return findById(propertyId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND));
    }

    @Query(
            "SELECT p FROM Property p WHERE p.startDate <= :currentDate AND p.endDate >= :currentDate")
    List<Property> findByDateRangeTrue(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT p FROM Property p WHERE p.startDate > :currentDate OR p.endDate < :currentDate")
    List<Property> findByDateRangeFalse(@Param("currentDate") LocalDate currentDate);

    /**
     * 데이터가 가장 많은 엔티티를 fetch join을 통해서 가져오고 나머지 엔티티에 대해서는 batch_fetch_size를 통해서 in 쿼리로 성능 챙기기
     *
     * @param propertyId
     * @return
     */
    @Query("SELECT p FROM Property p " + "JOIN FETCH p.areas " + "WHERE p.id = :propertyId")
    Optional<Property> findByPropertyIdUsingFetchJoin(Long propertyId);

    default Property getByPropertyUsingFetchJoin(Long propertyId) {
        return findByPropertyIdUsingFetchJoin(propertyId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND));
    }

    Page<Property> findByAdmin_Id(Long adminId, Pageable pageable);

    List<Property> findTop20ByOrderByLikeCountDesc();

    @Query("SELECT p.id FROM Property p WHERE p.id = :propertyId AND p.admin.id = :adminId")
    Long findIdByIdAndAdmin_Id(Long propertyId, Long adminId);

    default boolean existsByIdAndAdmin_id(Long propertyId, Long adminId) {
        if (findIdByIdAndAdmin_Id(propertyId, adminId) == null)
            throw new EntityNotFoundException(ErrorCode.NOT_FOUND);
        return true;
    }

    @Modifying
    @Transactional
    @Query("DELETE FROM Property p where p.id=:propertyId")
    int deletePropertyById(Long propertyId);

    @Query(
            "SELECT DISTINCT p FROM Property p "
                    + "LEFT JOIN p.areas a "
                    + "LEFT JOIN p.keywords k "
                    + "WHERE"
                    + "(LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) "
                    + "OR LOWER(p.areaAddr) LIKE LOWER(CONCAT('%', :search, '%')) "
                    + "OR LOWER(p.addrDo) LIKE LOWER(CONCAT('%', :search, '%')) "
                    + "OR LOWER(p.addrGu) LIKE LOWER(CONCAT('%', :search, '%')) "
                    + "OR LOWER(p.addrDong) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Property> findPropertiesBySearching(@Param("search") String search, Pageable pageable);

    @Query(
            "SELECT DISTINCT p FROM Property p "
                    + "LEFT JOIN p.areas a "
                    + "LEFT JOIN p.keywords k "
                    + "WHERE "
                    + "(:area IS NULL OR LOWER(p.addrDo) IN :area) "
                    + "AND (:propertyType IS NULL OR p.propertyType IN :propertyType) "
                    + "AND (:salesType IS NULL OR p.salesType IN :salesType) "
                    + "AND (:keywords IS NULL OR EXISTS (SELECT 1 FROM p.keywords k WHERE k.name IN :keywords))"
                    + "AND (:priceMin IS NULL OR EXISTS (SELECT 1 FROM p.areas a WHERE a.price >= :priceMin OR a.discountPrice >= :priceMin)) "
                    + "AND (:priceMax IS NULL OR EXISTS (SELECT 1 FROM p.areas a WHERE a.price <= :priceMax OR a.discountPrice <= :priceMax)) "
                    + "AND (:areaMin IS NULL OR EXISTS (SELECT 1 FROM p.areas a WHERE a.squareMeter >= :areaMin)) "
                    + "AND (:areaMax IS NULL OR EXISTS (SELECT 1 FROM p.areas a WHERE a.squareMeter <= :areaMax)) "
                    + "AND (:totalMin IS NULL OR p.totalNumber >= :totalMin) "
                    + "AND (:totalMax IS NULL OR p.totalNumber <= :totalMax)")
    Page<Property> findPropertiesByFiltering(
            @Param("area") List<String> area,
            @Param("propertyType") List<PropertyType> propertyType,
            @Param("salesType") List<SalesType> salesType,
            @Param("keywords") List<KeywordName> keywords,
            @Param("priceMin") Integer priceMin,
            @Param("priceMax") Integer priceMax,
            @Param("areaMin") Integer areaMin,
            @Param("areaMax") Integer areaMax,
            @Param("totalMin") Integer totalMin,
            @Param("totalMax") Integer totalMax,
            Pageable pageable);
}
