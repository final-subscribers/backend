package subscribers.clearbunyang.domain.property.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import subscribers.clearbunyang.domain.property.entity.Property;

@SpringBootTest
class PropertyRepositoryTest {
    @Autowired PropertyRepository propertyRepository;

    @BeforeEach
    void setUp() {}

    @Test
    void findByDateRange() {
        PageRequest request = PageRequest.of(0, 10);
        Page<Property> byDateRange =
                propertyRepository.findByDateRange(LocalDate.now(), request, true);

        List<Property> content = byDateRange.getContent();
        for (Property property : content) {
            System.out.println(property.getId());
        }
    }
}
