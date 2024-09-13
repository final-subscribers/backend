package subscribers.clearbunyang;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ClearBunyangApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClearBunyangApplication.class, args);
    }
}
