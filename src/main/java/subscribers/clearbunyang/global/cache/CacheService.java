package subscribers.clearbunyang.global.cache;

public interface CacheService {
    <T> T getFromCache(String cacheName, String key, Class<T> type);

    void putInCache(String cacheName, Object value);

    void putInCache(String cacheName, Object value, Long TTL);

    void evictCache(String cacheName, String key);
}
