package de.muenchen.stadtbezirksbudget.backend.kafka;

import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_NO_SECURITY_PROFILE;
import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_TEST_PROFILE;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import de.muenchen.stadtbezirksbudget.backend.TestConstants;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles(profiles = { SPRING_TEST_PROFILE, SPRING_NO_SECURITY_PROFILE })
@EmbeddedKafka(partitions = 1, topics = "${spring.kafka.template.default-topic}")
class KafkaConsumerServiceTest {
    @Container
    @ServiceConnection
    @SuppressWarnings("unused")
    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>(
            DockerImageName.parse(TestConstants.TESTCONTAINERS_POSTGRES_IMAGE));

    @Value("${spring.kafka.template.default-topic}")
    private String topic;
    @Autowired
    private KafkaTemplate<String, KafkaDTO> kafkaTemplate;
    @MockitoSpyBean
    private KafkaConsumerService kafkaConsumerService;

    @Nested
    class Listen {
        @Test
        void testListen() {
            final KafkaDTO kafkaDTO = new KafkaDTO("test name", "test message", 123);
            final String key = UUID.randomUUID().toString();
            kafkaTemplate.send(topic, key, kafkaDTO);
            verify(kafkaConsumerService, timeout(5000)).listen(key, kafkaDTO);
        }
    }
}
