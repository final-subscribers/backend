package subscribers.clearbunyang.domain.consultation.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import subscribers.clearbunyang.global.config.SecurityConfig;
import subscribers.clearbunyang.global.token.JwtTokenProcessor;
import subscribers.clearbunyang.security.annotation.WithMockCustomAdmin;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@DisplayName("통합 테스트")
@Slf4j
public class ConsultationControllerIntegrationTest {
    @Autowired private MockMvc mockMvc;

    @Autowired private ConsultationService consultationService;

    @MockBean private JwtTokenProcessor jwtTokenProcessor;

    @DisplayName("상담 수정 동시성 테스트")
    @Test
    @WithMockCustomAdmin
    public void consultation() throws Exception {

        int numberOfThreads = 5;
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        // When
        // log.info("동시성 테스트 진행");

        List<Future<String>> results = new ArrayList<>(); // 결과를 저장할 리스트

        for (int i = 0; i < numberOfThreads; i++) {
            Future<String> result =
                    service.submit(
                            () -> {
                                try {
                                    consultationService.registerConsultant(180L, "a-11");
                                    return "Success";
                                } catch (Exception e) {
                                    return "Failed: " + e.getMessage();
                                } finally {
                                    latch.countDown();
                                }
                            });
            results.add(result); // 결과를 리스트에 추가
        }

        latch.await();
        service.shutdown();

        for (Future<String> result : results) {
            System.out.println(result.get()); // 각 스레드의 결과 출력
        }
    }
}
