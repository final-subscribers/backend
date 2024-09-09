package subscribers.clearbunyang.domain.property.repository;


import io.lettuce.core.dynamic.annotation.Param;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.model.PropertyDateDto;
import subscribers.clearbunyang.domain.user.entity.Member;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;
import subscribers.clearbunyang.global.exception.notFound.EntityNotFoundException;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query(
            "SELECT new subscribers.clearbunyang.domain.property.model.PropertyDateDto(p.id, p.name, p.endDate, p.startDate) "
                    + "FROM Property p WHERE p.startDate <= :today AND p.endDate >= :today ORDER BY p.id DESC")
    List<PropertyDateDto> findPendingPropertiesDateDto(LocalDate today, Pageable pageable);

    default List<PropertyDateDto> getPendingPropertiesDto(LocalDate today, Pageable pageable) {
        List<PropertyDateDto> properties = findPendingPropertiesDateDto(today, pageable);
        return properties != null ? properties : Collections.emptyList();
    }

    @Query(
            "SELECT new subscribers.clearbunyang.domain.property.model.PropertyDateDto(p.id, p.name, p.endDate, p.startDate) "
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
            "SELECT p FROM Property p JOIN p.likes l WHERE l.member = :member AND "
                    + "(:isOpen = true AND p.startDate <= :currentDate AND p.endDate >= :currentDate) "
                    + "OR (:isOpen = false AND p.endDate < :currentDate)")
    Page<Property> findAllByMemberAndDateRange(
            @org.springframework.data.repository.query.Param("member") Member member,
            @org.springframework.data.repository.query.Param("currentDate") LocalDate currentDate,
            @org.springframework.data.repository.query.Param("pageable") Pageable pageable,
            @org.springframework.data.repository.query.Param("isOpen") boolean isOpen);

    /**
     * 데이터가 가장 많은 엔티티를 fetch join을 통해서 가져오고 나머지 엔티티에 대해서는 batch_fetch_size를 통해서 in 쿼리로 성능 챙기기
     *
     * @param propertyId
     * @return
     */
    // todo default_batch_fetch_size가 있어도 property와 1:n 관계인 엔티티들을 호출할떄마다 sql 쿼리문이 실행됨. 이거 맞아?
    @Query("SELECT p FROM Property p " + "JOIN FETCH p.areas " + "WHERE p.id = :propertyId")
    Optional<Property> findByPropertyIdUsingFetchJoin(Long propertyId);

    default Property getByPropertyUsingFetchJoin(Long propertyId) {
        return findByPropertyIdUsingFetchJoin(propertyId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND));
    }

    Page<Property> findByAdmin_Id(Long adminId, Pageable pageable);
}
