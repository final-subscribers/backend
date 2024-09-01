package subscribers.clearbunyang.domain.consultation.repository.dashboard;

import static subscribers.clearbunyang.domain.consultation.entity.QMemberConsultation.*;
import static subscribers.clearbunyang.domain.consultation.entity.enums.Status.*;
import static subscribers.clearbunyang.domain.property.entity.QProperty.*;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import subscribers.clearbunyang.domain.consultation.model.dashboard.ConsultationProgressDTO;

@Repository
public class DashboardRepositoryImpl implements DashboardRepository {

    private final JPAQueryFactory query;

    public Page<ConsultationProgressDTO> findConsultationProgress(Long userId, Pageable pageable) {
        List<ConsultationProgressDTO> list =
                query.select(
                                Projections.constructor(
                                        ConsultationProgressDTO.class,
                                        memberConsultation.property.id,
                                        memberConsultation.property.buildingName,
                                        memberConsultation
                                                .status
                                                .when(PENDING)
                                                .then(1)
                                                .otherwise(0)
                                                .sum()
                                                .intValue(),
                                        memberConsultation
                                                .status
                                                .when(COMPLETED)
                                                .then(1)
                                                .otherwise(0)
                                                .sum()
                                                .intValue()))
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

    public DashboardRepositoryImpl(EntityManager em) {
        query = new JPAQueryFactory(em);
    }
}
