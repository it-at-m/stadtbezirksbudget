package de.muenchen.stadtbezirksbudget.cit_eai.zammad;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for the Zammad integration.
 * <p>
 * Bound from properties with the prefix "zammad" (e.g. zammad.base-path).
 */

@ConfigurationProperties(prefix = "zammad")
@Validated
@Getter
@RequiredArgsConstructor(onConstructor_ = @ConstructorBinding)
public class ZammadProperties {
    @NotBlank private final String basePath;
}
