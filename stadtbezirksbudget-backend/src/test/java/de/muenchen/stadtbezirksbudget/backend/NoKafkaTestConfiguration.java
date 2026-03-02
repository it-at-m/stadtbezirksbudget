package de.muenchen.stadtbezirksbudget.backend;

import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

/**
 * Test configuration to disable Kafka in tests that do not require it. It imports the
 * TestChannelBinderConfiguration to provide a test binder for Spring Cloud
 * Stream, which allows the application context to load without requiring a real Kafka broker.
 */
@Configuration
@Import(TestChannelBinderConfiguration.class)
@Profile("!kafka-test")
public class NoKafkaTestConfiguration {
}
