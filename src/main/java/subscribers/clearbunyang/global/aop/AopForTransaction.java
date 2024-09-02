package subscribers.clearbunyang.global.aop;


import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

// AOP에서 트랜잭션 분리를 위한 클래스
@Component
public class AopForTransaction {

    // Propagation.REQUIRES_NEW 옵션을 지정해 부모 트랜잭션의 유무에 관계없이 별도의 트랜잭션으로 동작하게끔 설정
    @Transactional(propagation = Propagation.REQUIRES_NEW, timeout = 8) // lease time 보다 짧게 시간 설정.
    public Object proceed(final ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }
}
