package de.muenchen.stadtbezirksbudget.cit_eai.zammad;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for the Zammad integration.
 * <p>
 * Bound from properties with the prefix "spring.zammad" (e.g. spring.zammad.base-path).
 */
@Component
@ConfigurationProperties(prefix = "spring.zammad")
@Validated
@Getter
public class ZammadProperties {

    @NotBlank private String basePath;

    @NotBlank private String clientId;
}
