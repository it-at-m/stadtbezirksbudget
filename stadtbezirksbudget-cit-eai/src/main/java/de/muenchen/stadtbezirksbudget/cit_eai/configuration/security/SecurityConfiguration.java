package de.muenchen.stadtbezirksbudget.cit_eai.configuration.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 * Configuration for OAuth2 security.
 */
@Configuration
@Profile("!no-security")
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
}
