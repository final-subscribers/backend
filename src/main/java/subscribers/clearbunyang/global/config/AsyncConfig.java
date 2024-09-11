package subscribers.clearbunyang.global.config;


import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Bean(name = "mailExecutor")
    public Executor mailExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // 기본 스레드 풀 크기
        executor.setMaxPoolSize(20); // 최대 스레드 풀 크기
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("MailExecutor-"); // 스레드 이름
        // 데코레이터 적용
        executor.setTaskDecorator(new AsyncDecorator());

        // 거부 작업 처리
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        executor.initialize();
        return executor;
    }

    @Bean(name = "smsExecutor")
    public Executor smsExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // 기본 스레드 풀 크기
        executor.setMaxPoolSize(20); // 최대 스레드 풀 크기
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("SmsExecutor-"); // 스레드 이름
        // 데코레이터 적용
        executor.setTaskDecorator(new AsyncDecorator());

        // 거부 작업 처리
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        executor.initialize();
        return executor;
    }

    @Bean(name = "likesExecutor")
    public Executor likesExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // 기본 스레드 풀 크기
        executor.setMaxPoolSize(20); // 최대 스레드 풀 크기
        executor.setQueueCapacity(100); // 큐 용량
        executor.setKeepAliveSeconds(60); // 스레드 유지 시간
        executor.setThreadNamePrefix("LikesExecutor-"); // 스레드 이름 접두사

        // 데코레이터 적용
        executor.setTaskDecorator(new AsyncDecorator());

        // 작업 거부 처리
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncHandler();
    }
}
