package de.muenchen.stadtbezirksbudget.cit_eai.zammad;

import de.muenchen.stadtbezirksbudget.cit_eai.zammad.api.TicketsApi;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@AutoConfiguration
@RequiredArgsConstructor
public class ZammadApiConfig {

    private final ZammadProperties zammadProperties;

    @Bean
    protected ApiClient apiClient(final ClientRegistrationRepository clientRegistrationRepository,
            final OAuth2AuthorizedClientService auth2AuthorizedClientService) {
        final ServletOAuth2AuthorizedClientExchangeFilterFunction oauth = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                        clientRegistrationRepository, auth2AuthorizedClientService));

        oauth.setDefaultClientRegistrationId(zammadProperties.getClientId());
        final WebClient webClient = ApiClient.buildWebClientBuilder().apply(oauth.oauth2Configuration()).build();
        final ApiClient apiClient = new ApiClient(webClient);

        apiClient.setBasePath(zammadProperties.getBasePath());

        return apiClient;
    }

    @Bean
    protected TicketsApi ticketsApi(final ApiClient apiClient) {
        return new TicketsApi(apiClient);
    }
}
