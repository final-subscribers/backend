package subscribers.clearbunyang.global.exception.errorCode;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum ErrorCode {

    // Common
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "400", "요청 파라미터나, 요청 바디의 값을 다시 확인하세요"),
    JSON_CONVERSION_FAILED(
            HttpStatus.INTERNAL_SERVER_ERROR.value(), "500", "JSON 형식이 아니라 변환에 실패했습니다"),
    INVALID_INPUT_VALUE(400, "C001", "유효하지 않은 입력입니다."),
    NOT_FOUND(404, "C002", "Not Found"),
    NO_QUERY_RESULT(404, "C003", "No Query Result"),
    DATETIME_INVALID(400, "C004", "유효하지 않은 날짜입니다"),

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

    // Consultation
    TIER_CANNOT_BE_SPECIFIED(400, "A002", "등급을 입력 할 수 없습니다"),
    UNABLE_TO_CHANGE_CONSULTANT(HttpStatus.BAD_REQUEST.value(), "A003", "상담사를 변경할 수 없습니다"),
    LOCK_AQUISITION_FAILED(HttpStatus.CONFLICT.value(), "A004", "락 획득에 실패했습니다"),

    // jwt
    INVALID_TOKEN(400, "J001", "토큰이 올바르지 않습니다."),
    REFRESH_TOKEN_NOT_FOUND(404, "J005", "리프레쉬 토큰 정보가 존재하지 않습니다."),
    INVALID_ACCESS_TOKEN(400, "J007", "엑세스 토큰이 올바르지 않습니다."),

    // like
    PROPERTY_NOT_FOUND(404, "L002", "존재하지 않는 물건 정보입니다."),

    // file
    FILE_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "F001", "파일 타입이 존재하지 않습니다"),

    // area
    AREA_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "AR001", "세대 면적 정보가 존재하지 않습니다."),
    ;

    private final Integer status;
    private final String code;
    private final String message;
}
