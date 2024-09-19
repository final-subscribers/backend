package subscribers.clearbunyang.global.config;


import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import subscribers.clearbunyang.global.security.filter.AuthenticationFilter;
import subscribers.clearbunyang.global.security.token.JwtTokenProcessor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProcessor jwtTokenProcessor;

    @Value("${deploy.server.port}") private String allowedServerIpv4Address;

    @Value("${deploy.monitoring-server.port}") private String allowedMonitoringIpv4Address;

    private List<IpAddressMatcher> ipv4AddressMatchers;

    @PostConstruct
    public void init() {
        this.ipv4AddressMatchers =
                Arrays.asList(allowedServerIpv4Address, allowedMonitoringIpv4Address).stream()
                        .map(ip -> new IpAddressMatcher(ip + "/32"))
                        .collect(Collectors.toList());
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("^(?!/api/).*"); // api 엔드포인트
    }

    // swagger 설정 나중에 추가
    private final String[] swagger = {
        "/open-api/**",
        "/resources/**",
        "/error",
        "/swagger-resources/**",
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/v3/api-docs"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        auth ->
                                auth.requestMatchers(swagger)
                                        .permitAll()
                                        .requestMatchers("/api/auth/**")
                                        .permitAll()
                                        .requestMatchers("/api/common/**")
                                        .permitAll()
                                        .requestMatchers("/api/admin/**")
                                        .hasAuthority("ADMIN")
                                        .requestMatchers("/api/member/**")
                                        .hasAuthority("MEMBER")
                                        .requestMatchers("/actuator/**")
                                        .access(this::hasIpAddress)
                                        .anyRequest()
                                        .authenticated())
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(
                        exception ->
                                exception.authenticationEntryPoint(
                                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationFilter authenticationFilter() {
        return new AuthenticationFilter(jwtTokenProcessor);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private AuthorizationDecision hasIpAddress(
            Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        String requestIp = object.getRequest().getRemoteAddr();
        boolean isIpMatched =
                ipv4AddressMatchers.stream().anyMatch(matcher -> matcher.matches(requestIp));

        return new AuthorizationDecision(
                (authentication.get() instanceof AnonymousAuthenticationToken) && isIpMatched);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        List<String> allowed =
                Arrays.asList(
                        "http://localhost:5173",
                        "https://entj.site",
                        "https://final-project-l15zu1wpp-yeojins-projects-a26b6f35.vercel.app",
                        "https://final-project-eta-silk.vercel.app");
        configuration.setAllowedOrigins(allowed);
        configuration.setAllowedMethods(
                Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(
                Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
