package de.muenchen.stadtbezirksbudget.cit_eai.configuration;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Configuration properties for WebClient timeouts.
 */
@ConfigurationProperties(prefix = "webclient.timeout")
public record WebClientTimeoutProperties(
        @DefaultValue("30s") Duration readTimeout,
        @DefaultValue("30s") Duration writeTimeout,
        @DefaultValue("30s") Duration responseTimeout) {
}
