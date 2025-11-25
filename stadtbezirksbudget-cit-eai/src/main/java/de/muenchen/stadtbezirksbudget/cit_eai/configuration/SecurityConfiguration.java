package de.muenchen.stadtbezirksbudget.cit_eai.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 * Configuration for OAuth2 security.
 */
@Configuration
@Profile("!no-security")
@RequiredArgsConstructor
public class SecurityConfiguration {

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
     * Configures the security filter chain in local profile to permit all requests without
     * authentication.
     *
     * @param http the HttpSecurity object used to configure web-based security
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Profile("local")
    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http
                .headers(customizer -> customizer
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(requests -> requests
                        .anyRequest()
                        .permitAll())
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
