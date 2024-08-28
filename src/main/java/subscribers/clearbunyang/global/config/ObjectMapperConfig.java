package subscribers.clearbunyang.global.config;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapperConfig = new ObjectMapper();
        objectMapperConfig.registerModule(new Jdk8Module());
        objectMapperConfig.registerModule(new JavaTimeModule());
        objectMapperConfig.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapperConfig.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapperConfig.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapperConfig.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
        return objectMapperConfig;
    }
}
