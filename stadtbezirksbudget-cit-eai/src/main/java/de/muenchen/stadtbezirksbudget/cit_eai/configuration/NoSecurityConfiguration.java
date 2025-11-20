package de.muenchen.stadtbezirksbudget.cit_eai.configuration;

import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class NoSecurityConfiguration {

    private final WebClientTimeoutProperties webClientTimeoutProperties;

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

    @Bean
    public WebClient webClient() {
        final HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(webClientTimeoutProperties.responseTimeout()))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(webClientTimeoutProperties.readTimeout()))
                        .addHandlerLast(new WriteTimeoutHandler(webClientTimeoutProperties.writeTimeout())));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
