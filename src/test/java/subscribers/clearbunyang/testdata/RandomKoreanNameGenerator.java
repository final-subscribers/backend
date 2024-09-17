package subscribers.clearbunyang.testdata;


import java.util.Random;

public class RandomKoreanNameGenerator {

    private static final String[] LAST_NAMES = {
        "김", "이", "박", "최", "정", "강", "조", "윤", "장", "임", "오", "한", "신", "서", "권", "황", "안", "송",
        "류", "홍"
    };

    private static final String[] FIRST_NAME_SYLLABLES = {
        "가", "강", "건", "경", "고", "관", "광", "구", "권", "규", "기",
        "나", "남", "노", "다", "동", "두", "라", "려", "명", "민", "미",
        "바", "백", "범", "병", "보", "성", "소", "수", "승", "시", "아",
        "연", "영", "예", "오", "용", "우", "원", "윤", "은", "이", "재",
        "정", "제", "준", "지", "진", "찬", "채", "현", "혜", "호", "훈"
    };

    public static String generateRandomName() {
        Random random = new Random();

        // 성 (한 글자) 랜덤 선택
        String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];

        // 이름 (두 글자) 랜덤 선택
        String firstName =
                FIRST_NAME_SYLLABLES[random.nextInt(FIRST_NAME_SYLLABLES.length)]
                        + FIRST_NAME_SYLLABLES[random.nextInt(FIRST_NAME_SYLLABLES.length)];

        // 완성된 이름 반환
        return lastName + firstName;
    }
}
