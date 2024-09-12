package subscribers.clearbunyang.domain.consultation.repository.dashboard;

import static subscribers.clearbunyang.domain.consultation.entity.QMemberConsultation.*;
import static subscribers.clearbunyang.domain.consultation.entity.enums.Medium.*;
import static subscribers.clearbunyang.domain.consultation.entity.enums.Status.*;
import static subscribers.clearbunyang.domain.consultation.entity.enums.dashboard.Phase.*;
import static subscribers.clearbunyang.domain.property.entity.QProperty.*;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import subscribers.clearbunyang.domain.consultation.entity.enums.Medium;
import subscribers.clearbunyang.domain.consultation.entity.enums.Status;
import subscribers.clearbunyang.domain.consultation.entity.enums.dashboard.Phase;
import subscribers.clearbunyang.domain.consultation.model.dashboard.ConsultationDateStatsDTO;
import subscribers.clearbunyang.domain.consultation.model.dashboard.PropertyInquiryDetailsDTO;
import subscribers.clearbunyang.domain.consultation.model.dashboard.PropertyInquiryStatusDTO;
import subscribers.clearbunyang.domain.consultation.model.dashboard.PropertySelectDTO;
import subscribers.clearbunyang.global.entity.YearWeek;

@Repository
public class DashboardRepositoryImpl implements DashboardRepository {

    private final JPAQueryFactory query;
    NumberExpression<Integer> pendingCount = statusSumExpression(PENDING);
    NumberExpression<Integer> completedCount = statusSumExpression(COMPLETED);

    public List<PropertySelectDTO> findDropdownSelects(Long adminId, Phase phase) {
        return query.select(
                        Projections.constructor(
                                PropertySelectDTO.class, property.id, property.name))
                .from(property)
                .where(property.admin.id.eq(adminId), phaseEq(phase))
                .orderBy(property.createdAt.desc())
                .fetch();
    }

    public PropertyInquiryStatusDTO findTodayStats(Long adminId) {
        Optional<PropertyInquiryStatusDTO> result =
                Optional.ofNullable(
                        query.select(
                                        Projections.constructor(
                                                PropertyInquiryStatusDTO.class,
                                                pendingCount,
                                                completedCount))
                                .from(memberConsultation)
                                .where(
                                        memberConsultation.property.admin.id.eq(adminId),
                                        memberConsultation.createdAt.between(
                                                LocalDate.now().atStartOfDay(),
                                                LocalDate.now()
                                                        .plusDays(1)
                                                        .atStartOfDay()
                                                        .minusNanos(1)))
                                .fetchOne());

        return result.orElse(new PropertyInquiryStatusDTO(0, 0));
    }

    public List<ConsultationDateStatsDTO> findTotalStatsByWeek(Long adminId) {
        // 현재 날짜 기준으로 지난 다섯 주의 날짜를 계산합니다.
        LocalDate now = LocalDate.now();
        List<YearWeek> lastFiveWeeks =
                IntStream.rangeClosed(0, 4)
                        .mapToObj(i -> YearWeek.from(now.minusWeeks(i)))
                        .sorted() // 오래된 순으로 정렬
                        .toList();

        // 데이터베이스에서 집계 데이터를 가져옵니다.
        List<ConsultationDateStatsDTO> stats =
                query.select(
                                Projections.constructor(
                                        ConsultationDateStatsDTO.class,
                                        memberConsultation.createdAt.year(),
                                        memberConsultation.createdAt.week(),
                                        pendingCount,
                                        completedCount))
                        .from(memberConsultation)
                        .where(
                                memberConsultation
                                        .property
                                        .admin
                                        .id
                                        .eq(adminId)
                                        .and(
                                                memberConsultation.createdAt.between(
                                                        lastFiveWeeks
                                                                .get(0)
                                                                .atDay(DayOfWeek.MONDAY.getValue())
                                                                .atStartOfDay(),
                                                        lastFiveWeeks
                                                                .get(lastFiveWeeks.size() - 1)
                                                                .atDay(DayOfWeek.SUNDAY.getValue())
                                                                .plusDays(1)
                                                                .atStartOfDay()
                                                                .minusNanos(1))))
                        .groupBy(
                                memberConsultation.createdAt.year(),
                                memberConsultation.createdAt.week())
                        .fetch();

        // 가져온 데이터를 Map으로 변환하여 빠르게 검색할 수 있도록 합니다.
        Map<YearWeek, ConsultationDateStatsDTO> statsMap =
                stats.stream()
                        .collect(
                                Collectors.toMap(
                                        stat -> YearWeek.of(stat.getYear(), stat.getWeek()),
                                        Function.identity()));

        // 결과를 반환할 리스트를 생성합니다.
        List<ConsultationDateStatsDTO> result = new ArrayList<>();

        // 지난 다섯 주를 순회하면서 해당 주차에 데이터가 있으면 추가하고, 없으면 0으로 채웁니다.
        for (YearWeek week : lastFiveWeeks) {
            result.add(
                    statsMap.getOrDefault(
                            week,
                            new ConsultationDateStatsDTO(week.getYear(), week.getWeek(), 0, 0)));
        }

        return result;
    }

    public List<PropertyInquiryStatusDTO> findStatsOrderByCountDesc(Long adminId) {
        return query.select(
                        Projections.constructor(
                                PropertyInquiryStatusDTO.class,
                                memberConsultation.property.id,
                                memberConsultation.property.name,
                                pendingCount,
                                completedCount))
                .from(memberConsultation)
                .where(memberConsultation.property.admin.id.eq(adminId))
                .groupBy(memberConsultation.property.id)
                .orderBy(memberConsultation.count().desc())
                .fetch();
    }

    public Page<PropertyInquiryStatusDTO> findPropertiesInquiryStats(
            Long adminId, Pageable pageable) {

        List<PropertyInquiryStatusDTO> list =
                query.select(
                                Projections.constructor(
                                        PropertyInquiryStatusDTO.class,
                                        memberConsultation.property.id,
                                        memberConsultation.property.name,
                                        pendingCount,
                                        completedCount))
                        .from(memberConsultation)
                        .innerJoin(memberConsultation.property, property)
                        .where(memberConsultation.property.admin.id.eq(adminId))
                        .groupBy(memberConsultation.property.id)
                        .orderBy(memberConsultation.property.id.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        JPAQuery<Long> countQuery =
                query.select(memberConsultation.property.id.countDistinct())
                        .from(memberConsultation)
                        .where(memberConsultation.property.admin.id.eq(adminId));

        return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchOne);
    }

    @Override
    public PropertyInquiryDetailsDTO findPropertyInquiryDetails(
            Long propertyId, LocalDate start, LocalDate end) {

        NumberExpression<Integer> phoneCount = mediumSumExpression(PHONE);
        NumberExpression<Integer> channelCount = mediumSumExpression(CHANNEL);
        NumberExpression<Integer> lmsCount = mediumSumExpression(LMS);

        Optional<PropertyInquiryDetailsDTO> result =
                Optional.ofNullable(
                        query.select(
                                        Projections.constructor(
                                                PropertyInquiryDetailsDTO.class,
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

        return result.orElse(new PropertyInquiryDetailsDTO(0));
    }

    private BooleanExpression phaseEq(Phase phase) {
        LocalDate now = LocalDate.now();
        if (phase == OPEN) {
            return property.startDate.loe(now).and(property.endDate.gt(now));
        } else if (phase == CLOSED) {
            return property.endDate.lt(now).or(property.startDate.gt(now));
        }
        return null;
    }

    private NumberExpression<Integer> statusSumExpression(Status status) {
        return memberConsultation.status.when(status).then(1).otherwise(0).sum().coalesce(0);
    }

    private NumberExpression<Integer> mediumSumExpression(Medium medium) {
        return memberConsultation.medium.when(medium).then(1).otherwise(0).sum().coalesce(0);
    }

    public DashboardRepositoryImpl(EntityManager em) {
        query = new JPAQueryFactory(em);
    }
}
