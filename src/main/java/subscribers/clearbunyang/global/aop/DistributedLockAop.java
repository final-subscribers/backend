package subscribers.clearbunyang.global.aop;


import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionTimedOutException;
import subscribers.clearbunyang.domain.consultation.exception.ConsultationException;
import subscribers.clearbunyang.global.annotation.DistributedLock;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAop {

    private static final String LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    // 실질적인 락 동작
    @Around("@annotation(subscribers.clearbunyang.global.annotation.DistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        String key =
                LOCK_PREFIX
                        + getDynamicValue(
                                methodSignature.getParameterNames(),
                                joinPoint.getArgs(),
                                distributedLock.key()); // 키 설정
        RLock rLock = redissonClient.getLock(key); // 1

        boolean available = false;

        try {
            available =
                    rLock.tryLock(
                            distributedLock.waitTime(),
                            distributedLock.leaseTime(),
                            distributedLock.timeUnit()); // (2)
            if (!available) {
                log.info("락 획득 실패={}", key); // TODO Exception 변경
                throw new ConsultationException(
                        ErrorCode.BAD_REQUEST); // ClassCastException 는 return false, return 0 일 때
                // 발생.
            }
            log.info("로직 수행");
            return aopForTransaction.proceed(joinPoint); // (3)
        } catch (InterruptedException e) {
            log.info("에러 발생");
            throw e;
        } catch (TransactionTimedOutException e) {
            log.info("트랜잭션 타임아웃");
            throw e;
        } finally { // try catch 가 없다면 테스트상 접근 하나 통과, 로직 수행 log 한 번
            // try { // try catch 가 있으면 테스트상 모든 접근 통과, 콘솔상 로직 수행이라는 log 한 번 나옴
            rLock.unlock(); // (4) 획득 유무와 상관 없이 unlock
            // } catch (IllegalMonitorStateException e) {
            log.info("락 해제중  {} {}", method.getName(), key);
            //  }
        }
    }

    // 전달받은 Lock의 이름을 Spring Expression Language 로 파싱하여 읽어옴
    private static Object getDynamicValue(String[] parameterNames, Object[] args, String key) {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }
        return parser.parseExpression(key).getValue(context, Object.class);
    }
}
