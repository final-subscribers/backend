package subscribers.clearbunyang;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordType;

@SpringBootTest
public class EnumTest {
    @Test
    public void test() {
        KeywordType keywordType = KeywordType.SUBWAY;
        System.out.println(keywordType.name());
    }
}
