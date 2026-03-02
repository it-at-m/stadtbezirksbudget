package de.muenchen.stadtbezirksbudget.backend;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class IntegrationTestConfiguration {
    @ServiceConnection
    @Bean
    protected ConfluentKafkaContainer kafkaContainer() {
        return new ConfluentKafkaContainer(DockerImageName.parse(TestConstants.TESTCONTAINERS_KAFKA_IMAGE));
    }

    @ServiceConnection
    @Bean
    protected PostgreSQLContainer<?> postgreSqlContainer() {
        return new PostgreSQLContainer<>(DockerImageName.parse(TestConstants.TESTCONTAINERS_POSTGRES_IMAGE));
    }
}
