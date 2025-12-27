package de.muenchen.stadtbezirksbudget.cit_eai.configuration.security;

import de.muenchen.stadtbezirksbudget.cit_eai.common.ExcludedFromGeneratedCoverage;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 * Configuration for OAuth2 security.
 */
@ExcludedFromGeneratedCoverage(reason = "Too complex to test")
@Configuration
@Profile("!no-security")
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
@Slf4j
public class SecurityConfiguration {
    private final Optional<KeycloakRolesAuthoritiesConverter> keycloakRolesAuthoritiesConverter;
    private final Optional<UserInfoAuthoritiesConverter> userInfoAuthoritiesConverter;

    /**
     * Creates an OAuth2AuthorizedClientManager bean.
     *
     * @param clientRegistrationRepository the repository of OAuth2 client registrations used to look up
     *            client configurations
     * @param authorizedClientService the service responsible for storing and retrieving authorized
     *            OAuth2 clients (e.g. access and refresh tokens)
     * @return the OAuth2AuthorizedClientManager
     */
    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            final ClientRegistrationRepository clientRegistrationRepository, final OAuth2AuthorizedClientService authorizedClientService) {
        return new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientService);
    }

    /**
     * Creates a WebClient bean configured for OAuth2 authorization.
     *
     * @param authorizedClientManager the OAuth2AuthorizedClientManager used to manage authorized
     *            clients
     * @param registrationId the registration ID of the OAuth2 client to use for authorization
     * @param httpClient the HttpClient to be used by the WebClient
     * @return the authorized WebClient
     */
    @Bean
    public WebClient authorizedWebClient(
            final OAuth2AuthorizedClientManager authorizedClientManager,
            @Value("${oauth.registrationId}") final String registrationId,
            final HttpClient httpClient) {
        final ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
                authorizedClientManager);
        oauth2.setDefaultClientRegistrationId(registrationId);

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .apply(oauth2.oauth2Configuration())
                .build();
    }

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
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(
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
                            }
                            // DEPRECATED: authorities via userinfo endpoint
                            else if (userInfoAuthoritiesConverter.isPresent()) {
                                jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
                                        userInfoAuthoritiesConverter.get());
                            } else {
                                log.warn("No custom authority converter available, falling back to default.");
                            }
                            jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter);
                        }))
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
