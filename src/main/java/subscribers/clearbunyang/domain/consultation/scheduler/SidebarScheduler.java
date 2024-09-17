package subscribers.clearbunyang.domain.consultation.scheduler;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SidebarScheduler {

    @Scheduled(cron = " 0 0 0 * * ?") // 매일 자정에 실행 0 0 0 * * ?
    @CacheEvict(value = "sidebarList", allEntries = true)
    public void sidebarCache() {
        log.info("sidebarList 캐시 무효화");
    }
}
