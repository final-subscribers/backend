package subscribers.clearbunyang;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import subscribers.clearbunyang.domain.property.service.PropertyService;

@SpringBootTest
public class KeywordTest {
    @Autowired
    private PropertyService propertyService;

    @Test
    public void test() {
    }
}
