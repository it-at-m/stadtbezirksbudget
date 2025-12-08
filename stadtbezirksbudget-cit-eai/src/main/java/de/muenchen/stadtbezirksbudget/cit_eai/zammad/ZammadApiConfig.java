package de.muenchen.stadtbezirksbudget.cit_eai.zammad;

import de.muenchen.stadtbezirksbudget.cit_eai.zammad.generated.ApiClient;
import de.muenchen.stadtbezirksbudget.cit_eai.zammad.generated.api.TicketsApi;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class ZammadApiConfig {
    private final ZammadProperties zammadProperties;
    private final WebClient webClient;

    @Bean
    public ApiClient apiClient() {
        final ApiClient apiClient = new ApiClient(webClient);
        apiClient.setBasePath(zammadProperties.getBasePath());
        return apiClient;
    }

    @Bean
    public TicketsApi ticketsApi(final ApiClient apiClient) {
        return new TicketsApi(apiClient);
    }
}
