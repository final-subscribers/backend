package subscribers.clearbunyang.global.service;

import static subscribers.clearbunyang.global.config.RedisConfig.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedissonCacheServiceImpl implements CacheService {

    private final RedissonClient redissonClient;

    @Override
    public <T> T getFromCache(String cacheName, String key, Class<T> type) {
        RMapCache<String, T> mapCache = redissonClient.getMapCache(cacheName);
        return mapCache.get(key);
    }

    @Override
    public void putInCache(String cacheName, Object value) {
        RMapCache<String, Object> mapCache = redissonClient.getMapCache(cacheName);
        mapCache.put(cacheName, value, DEFAULT_CACHE_TTL, TimeUnit.MINUTES);
    }

    @Override
    public void putInCache(String cacheName, Object value, Long TTL) {
        RMapCache<String, Object> mapCache = redissonClient.getMapCache(cacheName);
        mapCache.put(cacheName, value, TTL, TimeUnit.MINUTES);
    }

    @Override
    public void evictCache(String cacheName, String key) {
        RMapCache<String, Object> mapCache = redissonClient.getMapCache(cacheName);
        mapCache.remove(key);
    }

    private List<Object> collectArguments(Object... args) {
        ArrayList<Object> collectedArgs = new ArrayList<>();
        try {
            // 현재 메서드 정보를 Stack Trace를 통해 얻음
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            String methodName = stackTrace[2].getMethodName(); // 호출한 메서드 이름

            // 현재 클래스에서 호출된 메서드의 파라미터 타입을 자동으로 추출
            Method[] methods = this.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    // 해당 메서드의 파라미터를 컬렉션에 추가
                    collectedArgs.addAll(Arrays.asList(args));
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return collectedArgs;
    }
}
