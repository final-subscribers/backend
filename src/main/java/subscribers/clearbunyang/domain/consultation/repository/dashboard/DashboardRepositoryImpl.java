package subscribers.clearbunyang.domain.consultation.repository.dashboard;

import static subscribers.clearbunyang.domain.consultation.entity.QMemberConsultation.*;
import static subscribers.clearbunyang.domain.consultation.entity.enums.Medium.*;
import static subscribers.clearbunyang.domain.consultation.entity.enums.Status.*;
import static subscribers.clearbunyang.domain.property.entity.QProperty.*;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import subscribers.clearbunyang.domain.consultation.entity.enums.Medium;
import subscribers.clearbunyang.domain.consultation.entity.enums.Status;
import subscribers.clearbunyang.domain.consultation.model.dashboard.PropertiesInquiryStatsDTO;
import subscribers.clearbunyang.domain.consultation.model.dashboard.PropertyInquiryDetailsDTO;

@Repository
public class DashboardRepositoryImpl implements DashboardRepository {

    private final JPAQueryFactory query;

    public Page<PropertiesInquiryStatsDTO> findConsultationProgress(
            Long userId, Pageable pageable) {
        NumberExpression<Integer> pendingCount = statusSumExpression(PENDING);
        NumberExpression<Integer> completedCount = statusSumExpression(COMPLETED);

        List<PropertiesInquiryStatsDTO> list =
                query.select(
                                Projections.constructor(
                                        PropertiesInquiryStatsDTO.class,
                                        memberConsultation.property.id,
                                        memberConsultation.property.buildingName,
                                        pendingCount,
                                        completedCount))
                        .from(memberConsultation)
                        .innerJoin(memberConsultation.property, property)
                        .where(memberConsultation.property.admin.id.eq(userId))
                        .groupBy(memberConsultation.property.id)
                        .orderBy(memberConsultation.property.id.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        JPAQuery<Long> countQuery =
                query.select(memberConsultation.property.id.countDistinct())
                        .from(memberConsultation)
                        .where(memberConsultation.property.admin.id.eq(userId));

        return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchOne);
    }

    @Override
    public PropertyInquiryDetailsDTO findPropertyInquiryDetails(
            Long propertyId, LocalDate start, LocalDate end) {
        NumberExpression<Integer> pendingCount = statusSumExpression(PENDING);
        NumberExpression<Integer> completedCount = statusSumExpression(COMPLETED);

        NumberExpression<Integer> phoneCount = mediumSumExpression(PHONE);
        NumberExpression<Integer> channelCount = mediumSumExpression(CHANNEL);
        NumberExpression<Integer> lmsCount = mediumSumExpression(LMS);

        Optional<PropertyInquiryDetailsDTO> result =
                Optional.ofNullable(
                        query.select(
                                        Projections.constructor(
                                                PropertyInquiryDetailsDTO.class,
                                                memberConsultation.property.buildingName,
                                                pendingCount,
                                                completedCount,
                                                phoneCount,
                                                channelCount,
                                                lmsCount))
                                .from(memberConsultation)
                                .where(
                                        memberConsultation.property.id.eq(propertyId),
                                        memberConsultation.createdAt.goe(start.atStartOfDay()),
                                        memberConsultation.createdAt.lt(
                                                end.plusDays(1L).atStartOfDay()))
                                .groupBy(memberConsultation.property.id)
                                .fetchOne());

        JPAQuery<LocalDateTime> stampsQuery =
                query.select(memberConsultation.createdAt)
                        .from(memberConsultation)
                        .where(
                                memberConsultation.property.id.eq(propertyId),
                                memberConsultation.createdAt.goe(start.atStartOfDay()),
                                memberConsultation.createdAt.lt(end.plusDays(1L).atStartOfDay()));

        result.ifPresent(detailsDTO -> detailsDTO.setTimeStamps(stampsQuery.fetch()));

        return result.orElse(
                new PropertyInquiryDetailsDTO(memberConsultation.property.buildingName.toString()));
    }

    private NumberExpression<Integer> statusSumExpression(Status status) {
        return memberConsultation.status.when(status).then(1).otherwise(0).sum().intValue();
    }

    private NumberExpression<Integer> mediumSumExpression(Medium medium) {
        return memberConsultation.medium.when(medium).then(1).otherwise(0).sum().intValue();
    }

    public DashboardRepositoryImpl(EntityManager em) {
        query = new JPAQueryFactory(em);
    }
}
