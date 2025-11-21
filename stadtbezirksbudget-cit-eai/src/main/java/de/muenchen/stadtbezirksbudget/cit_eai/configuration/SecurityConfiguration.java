package de.muenchen.stadtbezirksbudget.cit_eai.configuration;

import lombok.RequiredArgsConstructor;
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

@Configuration
@Profile("!no-security")
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            final ClientRegistrationRepository clientRegistrationRepository, final OAuth2AuthorizedClientService authorizedClientService) {
        return new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientService);
    }

    @Bean
    public WebClient authorizedWebClient(
            final OAuth2AuthorizedClientManager authorizedClientManager,
            @Value("${oauth.registrationId}") final String registrationId,
            @Value("${webclient.codec.maxInMemorySize:1048576}") final int maxInMemorySize,
            final HttpClient httpClient) {
        final ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
                authorizedClientManager);
        oauth2.setDefaultClientRegistrationId(registrationId);

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(clientCodecs -> clientCodecs.defaultCodecs().maxInMemorySize(maxInMemorySize))
                .apply(oauth2.oauth2Configuration())
                .build();
    }
}
