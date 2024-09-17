package subscribers.clearbunyang.testdata;


import java.util.Random;

public class RandomPhoneNumberGenerator {
    public static String generatePhoneNumber() {
        return "010" + generateRandomNumber(4) + generateRandomNumber(4);
    }

    // 지정된 자릿수의 랜덤 숫자를 생성하는 메소드
    private static String generateRandomNumber(int length) {
        Random random = new Random();
        int number = random.nextInt((int) Math.pow(10, length));
        return String.format("%04d", number); // 4자리로 맞춤
    }
}
