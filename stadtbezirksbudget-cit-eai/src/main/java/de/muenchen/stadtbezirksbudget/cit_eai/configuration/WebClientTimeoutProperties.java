package de.muenchen.stadtbezirksbudget.cit_eai.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "webclient.timeout")
public record WebClientTimeoutProperties(
        @DefaultValue("30") int readTimeout,
        @DefaultValue("30") int writeTimeout,
        @DefaultValue("30") int responseTimeout) {
}
