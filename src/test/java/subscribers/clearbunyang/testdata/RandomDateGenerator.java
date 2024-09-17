package subscribers.clearbunyang.testdata;


import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

public class RandomDateGenerator {
    // 두 날짜 사이의 랜덤한 날짜를 반환하는 메소드
    public static LocalDate genearateDate(LocalDate startDate, LocalDate endDate) {
        long startEpochDay = startDate.toEpochDay(); // 시작 날짜를 epoch day로 변환
        long endEpochDay = endDate.toEpochDay(); // 종료 날짜를 epoch day로 변환

        long randomEpochDay = ThreadLocalRandom.current().nextLong(startEpochDay, endEpochDay + 1);
        return LocalDate.ofEpochDay(randomEpochDay);
    }
}
