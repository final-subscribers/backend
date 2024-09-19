package subscribers.clearbunyang.global.RedissonLock;


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
import subscribers.clearbunyang.domain.consultation.exception.DistributedLockException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAop {

    private static final String LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;

    // 실질적인 락 동작
    @Around("@annotation(subscribers.clearbunyang.global.RedissonLock.DistributedLock)")
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

        boolean available;

        log.info("{} - 락 획득 시도", key);

        try {
            available =
                    rLock.tryLock(
                            distributedLock.waitTime(),
                            distributedLock.leaseTime(),
                            distributedLock.timeUnit());

            if (available) {
                log.info("락 획득 성공: {}", key);
                // 락을 성공적으로 획득한 경우에만 로직을 수행
                try {
                    log.info("실행: {}", key);
                    return joinPoint.proceed();
                } finally {
                    // 로직 수행 후 락 해제
                    try {
                        rLock.unlock();
                        log.info("락 해제 성공: {}", key);
                    } catch (IllegalMonitorStateException e) {
                        log.error("락 해제 실패: {} - 이미 해제되었거나 상태가 일치하지 않습니다.", key, e);
                    }
                }
            } else {
                log.warn("락 획득 실패: {}", key);
                throw new DistributedLockException(ErrorCode.LOCK_AQUISITION_FAILED) {};
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("락 획득 중 인터럽트 발생: {}", key, e);
            throw new DistributedLockException(ErrorCode.LOCK_AQUISITION_FAILED) {};
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
