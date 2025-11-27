package de.muenchen.stadtbezirksbudget.cit_eai.security;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;


/**
 * Configuration for OAuth2 security.
 */
@Configuration
@Profile("!no-security")
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
@Slf4j
public class SecurityConfiguration {

    private final Optional<KeycloakRolesAuthoritiesConverter> keycloakRolesAuthoritiesConverter;

    /**
     * Security filter chain configuration.
     *
     * @param http the HttpSecurity to configure
     * @return the security filter chain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests.requestMatchers(
                        // allow access to /actuator/info
                        PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.GET, "/actuator/info"),
                        // allow access to /actuator/health for OpenShift Health Check
                        PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.GET, "/actuator/health"),
                        // allow access to /actuator/health/liveness for OpenShift Liveness Check
                        PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.GET, "/actuator/health/liveness"),
                        // allow access to /actuator/health/readiness for OpenShift Readiness Check
                        PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.GET, "/actuator/health/readiness"),
                        // allow access to SBOM overview
                        PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.GET, "/actuator/sbom"),
                        // allow access to opean-api endpoints
                        PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.GET, "/v3/api-docs"),
                        PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.GET, "/v3/api-docs.yaml"),
                        PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.GET, "/v3/api-docs/**"),
                        // allow access to swagger-ui
                        PathPatternRequestMatcher.withDefaults().matcher("/swagger-ui/**"),
                        PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.GET, "/actuator/sbom"),
                        // allow access to SBOM application data
                        PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.GET, "/actuator/sbom/application"),
                        // allow access to /actuator/metrics for Prometheus monitoring in OpenShift
                        PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.GET, "/actuator/metrics"))
                        .permitAll())
                .authorizeHttpRequests((requests) -> requests
                        .anyRequest()
                        .authenticated())
                .oauth2ResourceServer(oAuth2ResourceServerConfigurer -> oAuth2ResourceServerConfigurer
                        .jwt(jwtConfigurer -> {
                            final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
                            // authorities via keycloak roles scope
                            if (keycloakRolesAuthoritiesConverter.isPresent()) {
                                jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
                                        keycloakRolesAuthoritiesConverter.get());
                            } else {
                                log.warn("No custom authority converter available, falling back to default.");
                            }
                            jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter);
                        }))
                // CSRF configuration to use cookies for storing CSRF tokens in bruno
                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));

        return http.build();
    }
}
