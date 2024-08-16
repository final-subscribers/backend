package subscribers.clearbunyang.domain.property.entity.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KeywordType {
    // 혜택
    DISCOUNT_SALE("BENEFIT"), // 할인 분양
    BALANCE_DEFERRAL("BENEFIT"), // 잔금 유예
    CASH_PAYMENT("BENEFIT"), // 현금 지급
    GUARANTEED_PAYMENT("BENEFIT"), // 게약금 안심 보장
    SUPPORT_PAYMENT("BENEFIT"), // 중도금 지원
    OPTION_PAYMENT("BENEFIT"), // 옵션 지급

    // 인프라
    SUBWAY("INFRA"), // 지하철역
    HOSPITAL("INFRA"), // 병원
    PARK("INFRA"), // 숲공원
    SHOPPING("INFRA"), // 쇼핑
    SCHOOL("INFRA"), // 학교
    LIBRARY("INFRA"), // 도서관
    PUBLIC_FACILITIES("INFRA"), // 공공시설
    GOVERNMENT("INFRA"), // 관공서
    ;
    private final String type;
}
