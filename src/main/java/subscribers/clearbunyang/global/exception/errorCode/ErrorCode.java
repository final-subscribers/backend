package subscribers.clearbunyang.global.exception.errorCode;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", "유효하지 않은 입력입니다."),
    NOT_FOUND(404, "C002", "Not Found"),
    DATETIME_INVALID(400, "C003", "유효하지 않은 날짜입니다"),

    // User
    EMAIL_DUPLICATION(400, "M001", "이미 등록된 회원 정보입니다."),
    PASSWORD_MISMATCH(401, "M002", "패스워드가 일치하지 않습니다."),
    USER_NOT_FOUND(404, "M003", "존재하지 않는 회원 정보입니다"),
    USER_EMAIL_INFO_NOT_FOUND(404, "M004", "등록된 이메일 아이디 정보가 존재하지 않습니다."),
    NOT_ACCEPTED_ADMIN(401, "M005", "승인되지 않은 관리자 계정입니다."),
    FILE_INFO_REQUIRED(404, "M006", "파일 정보가 존재하지 않습니다."),
    INVALID_FILE_INFO(400, "M007", "파일 정보가 올바르지 않습니다."),
    INVALID_VERIFICATION_CODE(400, "M008", "유효하지 않은 인증코드입니다."),
    INVALID_VERIFICATION_EMAIL(400, "M009", "인증되지 않은 이메일입니다."),
    PHONE_DUPLICATION(400, "M010", "이미 등록된 회원 정보입니다."),
    INVALID_VERIFICATION_SMS(400, "M011", "인증되지 않은 번호입니다."),

    // Admin
    PHONE_NUMBER_DUPLICATION(400, "A001", "이미 등록된 휴대폰 번호입니다"),
    TIER_CANNOT_BE_SPECIFIED(400, "A002", "등급을 입력 할 수 없습니다"),

    // jwt
    INVALID_TOKEN(400, "J001", "토큰이 올바르지 않습니다."),
    EXPIRED_TOKEN(400, "J002", "만료된 토큰 정보입니다."),
    UNSUPPORTED_TOKEN(400, "J003", "지원하지 않는 토큰 정보입니다."),
    CLAIMS_EMPTY_TOKEN(400, "J004", "토큰 정보가 비어있습니다."),
    REFRESH_TOKEN_NOT_FOUND(404, "J005", "리프레쉬 토큰 정보가 존재하지 않습니다."),
    INVALID_REFRESH_TOKEN(400, "J006", "리프레쉬 토큰이 올바르지 않습니다."),
    INVALID_ACCESS_TOKEN(400, "J007", "엑세스 토큰이 올바르지 않습니다."),

    // like
    PROPERTY_NOT_FOUND(404, "L002", "존재하지 않는 물건 정보입니다.");

    private final Integer status;
    private final String code;
    private final String message;
}
