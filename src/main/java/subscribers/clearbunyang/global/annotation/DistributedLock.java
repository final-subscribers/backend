package subscribers.clearbunyang.global.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    // 락 이름. 필수 설정
    String key();

    // 락의 단위: 초
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    // 락을 기다리는 시간. 락을 획득하기 위해 5초 대기
    long waitTime() default 20L; // 5(현업) ,10

    // 락 임대 시간. 3초 후에 락 해제
    long leaseTime() default 10L; // 3(현업) ,5보통 이걸 더 짧게
}
