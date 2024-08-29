package subscribers.clearbunyang.domain.consultation.repository.dashboard;

import static subscribers.clearbunyang.domain.consultation.entity.QMemberConsultation.*;
import static subscribers.clearbunyang.domain.consultation.entity.enums.Status.*;
import static subscribers.clearbunyang.domain.property.entity.QProperty.*;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import subscribers.clearbunyang.domain.consultation.model.dashboard.ConsultationProgressDTO;
import subscribers.clearbunyang.domain.user.entity.Admin;

public class DashboardRepositoryImpl implements DashboardRepository {

    private final JPAQueryFactory query;

    public Page<ConsultationProgressDTO> findConsultationProgress(Admin admin, Pageable pageable) {
        List<ConsultationProgressDTO> list =
                query.select(
                                Projections.constructor(
                                        ConsultationProgressDTO.class,
                                        memberConsultation.property.id,
                                        memberConsultation.property.buildingName,
                                        new CaseBuilder()
                                                .when(memberConsultation.status.eq(PENDING))
                                                .then(memberConsultation.count())
                                                .otherwise(0L)
                                                .sum(),
                                        new CaseBuilder()
                                                .when(memberConsultation.status.eq(COMPLETED))
                                                .then(memberConsultation.count())
                                                .otherwise(0L)
                                                .sum()))
                        .from(memberConsultation)
                        .innerJoin(memberConsultation.property, property)
                        .fetchJoin()
                        .where(memberConsultation.property.admin.id.eq(admin.getId()))
                        .groupBy(memberConsultation.property.id)
                        .orderBy(memberConsultation.property.createdAt.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        JPAQuery<Long> countQuery =
                query.select(memberConsultation.count())
                        .from(memberConsultation)
                        .where(memberConsultation.property.admin.id.eq(admin.getId()))
                        .groupBy(memberConsultation.property.id);

        return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchOne);
    }

    public DashboardRepositoryImpl(EntityManager em) {
        query = new JPAQueryFactory(em);
    }
}
