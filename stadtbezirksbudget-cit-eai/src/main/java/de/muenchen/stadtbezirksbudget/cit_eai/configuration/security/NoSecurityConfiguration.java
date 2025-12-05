package de.muenchen.stadtbezirksbudget.cit_eai.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 * Configures the security context to not require any authorization for incoming requests.
 */
@Configuration
@Profile("no-security")
@EnableWebSecurity
public class NoSecurityConfiguration {
    /**
     * Configures the security filter chain to disable frame options, permit all requests, and disable
     * CSRF protection.
     *
     * @param http the HttpSecurity object used to configure web-based security for specific requests
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
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

    /**
     * Creates a WebClient bean without any authorization configuration.
     *
     * @param httpClient the HttpClient to be used by the WebClient
     * @return the WebClient
     */
    @Bean
    public WebClient webClient(final HttpClient httpClient) {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
