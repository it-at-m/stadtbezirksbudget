package de.muenchen.stadtbezirksbudget.backend.configuration.frontend;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for the frontend application.
 */
@ConfigurationProperties(prefix = "frontend.config")
@Validated
@Data
public class FrontendConfigProperties {

    @NotBlank private String zammadBaseUrl;

    @NotBlank private String eakteBaseUrl;
}
