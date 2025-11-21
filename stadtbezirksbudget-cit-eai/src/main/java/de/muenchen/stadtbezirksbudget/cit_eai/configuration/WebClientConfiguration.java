package de.muenchen.stadtbezirksbudget.cit_eai.configuration;

import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfiguration {
    @Bean
    public HttpClient configuredHttpClient(final WebClientTimeoutProperties timeoutProperties) {
        return HttpClient.create()
                .responseTimeout(timeoutProperties.responseTimeout())
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(timeoutProperties.readTimeout().getSeconds(), TimeUnit.SECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(timeoutProperties.writeTimeout().getSeconds(), TimeUnit.SECONDS)));
    }
}
