package subscribers.clearbunyang.global.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        servers = {
            @Server(url = "https://entj.site/", description = "Default Server URL"),
            @Server(url = "http://localhost:8080/", description = "LocalHost Server URL")
        },
        info = @Info(title = "청약자들 명세서", description = "청약자들", version = "v1"))
@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "JWT";

    @Bean
    public ModelResolver modelResolver(ObjectMapper objectMapper) {
        return new ModelResolver(objectMapper);
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addSecurityItem(createSecurityRequirement())
                .components(createSecurityComponents())
                .path("/open-api/user/login", createLoginPath());
    }

    private SecurityRequirement createSecurityRequirement() {
        return new SecurityRequirement().addList(SECURITY_SCHEME_NAME);
    }

    private Components createSecurityComponents() {
        return new Components()
                .addSecuritySchemes(
                        SECURITY_SCHEME_NAME,
                        new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("Bearer")
                                .bearerFormat(SECURITY_SCHEME_NAME));
    }

    private PathItem createLoginPath() {
        Operation loginOperation =
                new Operation()
                        .tags(List.of("User"))
                        .summary("로그인")
                        .requestBody(createLoginRequestBody())
                        .responses(createLoginResponses());

        return new PathItem().post(loginOperation);
    }

    private RequestBody createLoginRequestBody() {
        return new RequestBody()
                .content(
                        new Content()
                                .addMediaType(
                                        "application/json",
                                        new MediaType()
                                                .schema(
                                                        new Schema<>()
                                                                .type("object")
                                                                .properties(
                                                                        Map.of(
                                                                                "email",
                                                                                        new Schema<>()
                                                                                                .type(
                                                                                                        "string"),
                                                                                "password",
                                                                                        new Schema<>()
                                                                                                .type(
                                                                                                        "string"))))));
    }

    private ApiResponses createLoginResponses() {
        return new ApiResponses()
                .addApiResponse("200", new ApiResponse().description("Successful login"))
                .addApiResponse("400", new ApiResponse().description("Bad request"));
    }
}
