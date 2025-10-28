package de.muenchen.stadtbezirksbudget.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Application class for starting the microservice.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
@EnableKafka
@SuppressWarnings("PMD.UseUtilityClass")
public class StadtbezirksbudgetBackend {
    public static void main(final String[] args) {
        SpringApplication.run(StadtbezirksbudgetBackend.class, args);
    }
}
