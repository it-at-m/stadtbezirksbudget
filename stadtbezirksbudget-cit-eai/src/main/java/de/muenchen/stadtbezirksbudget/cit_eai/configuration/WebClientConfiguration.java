package de.muenchen.stadtbezirksbudget.cit_eai.configuration;

import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.client.HttpClient;

@Configuration
@Slf4j
public class WebClientConfiguration {

    private String sanitizePath(final String path) {
        if (path == null) {
            return "";
        }
        final int idx = path.indexOf('/', 1);
        return (idx >= 0) ? path.substring(0, idx) : path;
    }

    /**
     * Configures the HttpClient with timeouts and logging.
     *
     * @param timeoutProperties the timeout properties
     * @return the configured HttpClient
     */
    @Bean
    public HttpClient configuredHttpClient(final WebClientTimeoutProperties timeoutProperties) {
        return HttpClient.create()
                .responseTimeout(timeoutProperties.responseTimeout())
                .doOnRequest((req, conn) -> log.debug("HTTP request -> method={} path={}", req.method(), sanitizePath(req.path())))
                .doOnResponse((res, conn) -> log.debug("HTTP response <- status={}", res.status()))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(timeoutProperties.readTimeout().toMillis(), TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(timeoutProperties.writeTimeout().toMillis(), TimeUnit.MILLISECONDS)));
    }
}
