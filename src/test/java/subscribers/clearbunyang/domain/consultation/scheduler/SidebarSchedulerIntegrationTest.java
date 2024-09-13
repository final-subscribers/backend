package subscribers.clearbunyang.domain.consultation.scheduler;

import static org.junit.Assert.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

@SpringBootTest
class SidebarSchedulerIntegrationTest {
    // 단위 테스트 환경에서는 스프링 컨텍스트가 로드되지 않아 AOP가 적용되지 않습니다.

    @Autowired private SidebarScheduler sidebarScheduler;

    @Autowired private CacheManager cacheManager;

    @Test
    void sidebarSchedulerTest() {
        // 캐시에 테스트 데이터 추가
        Cache cache = cacheManager.getCache("sidebarList");
        cache.put("testKey", "testValue");

        // 스케줄러 메서드 호출
        sidebarScheduler.sidebarCache();

        // 캐시가 비워졌는지 확인
        assertNull(cache.get("testKey"));
    }
}
