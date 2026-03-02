package de.muenchen.stadtbezirksbudget.backend.kafka;

import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_NO_SECURITY_PROFILE;
import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_TEST_PROFILE;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import de.muenchen.stadtbezirksbudget.backend.IntegrationTestConfiguration;
import de.muenchen.stadtbezirksbudget.backend.antrag.AntragService;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = { SPRING_TEST_PROFILE, SPRING_NO_SECURITY_PROFILE })
@Import(IntegrationTestConfiguration.class)
class KafkaBinderIntegrationTest {

    @Autowired
    private KafkaTemplate<String, KafkaDTO> kafkaTemplate;

    @MockitoSpyBean
    private AntragService antragService;

    @Value("${kafka.topic}")
    private String topic;

    @Nested
    class AntragConsumer {
        @Test
        void testValidMessage() throws Exception {
            final KafkaDTO validMessage = new KafkaDTO("Max Mustermann", "Münchner Bank", 1);
            kafkaTemplate.send(topic, UUID.randomUUID().toString(), validMessage).get();
            verify(antragService, timeout(5000)).createFromKafka(validMessage);

        }

        @Test
        void testInvalidMessageThrowsValidationException() {
            final KafkaDTO invalidMessage = new KafkaDTO("", "", 0);

            kafkaTemplate.send(topic, UUID.randomUUID().toString(), invalidMessage);
            verify(antragService, timeout(5000).times(0)).createFromKafka(invalidMessage);
        }
    }

}
