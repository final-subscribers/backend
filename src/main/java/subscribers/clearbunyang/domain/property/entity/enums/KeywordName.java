package subscribers.clearbunyang.domain.property.entity.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KeywordName {
    // 혜택
    DISCOUNT_SALE(KeywordType.BENEFIT), // 할인 분양
    BALANCE_DEFERRAL(KeywordType.BENEFIT), // 잔금 유예
    CASH_PAYMENT(KeywordType.BENEFIT), // 현금 지급
    GUARANTEED_PAYMENT(KeywordType.BENEFIT), // 게약금 안심 보장
    SUPPORT_PAYMENT(KeywordType.BENEFIT), // 중도금 지원
    OPTION_PAYMENT(KeywordType.BENEFIT), // 옵션 지급

    // 인프라
    SUBWAY(KeywordType.INFRA), // 지하철역
    HOSPITAL(KeywordType.INFRA), // 병원
    PARK(KeywordType.INFRA), // 숲공원
    SHOPPING(KeywordType.INFRA), // 쇼핑
    SCHOOL(KeywordType.INFRA), // 학교
    LIBRARY(KeywordType.INFRA), // 도서관
    PUBLIC_FACILITIES(KeywordType.INFRA), // 공공시설
    GOVERNMENT(KeywordType.INFRA), // 관공서
    ;
    private final KeywordType type;
}
