package de.muenchen.stadtbezirksbudget.cit_eai;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;

@SuppressWarnings("PMD.TestClassWithoutTestCases")
@TestConfiguration
public class TestWebClientConfig {
    @Bean
    @Primary
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}
