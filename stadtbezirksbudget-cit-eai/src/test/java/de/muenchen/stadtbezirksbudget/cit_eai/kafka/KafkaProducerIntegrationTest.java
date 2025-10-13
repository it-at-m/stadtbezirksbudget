package de.muenchen.stadtbezirksbudget.cit_eai.kafka;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.muenchen.stadtbezirksbudget.cit_eai.TestConstants;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, topics = "${spring.kafka.template.default-topic}")
@ActiveProfiles(TestConstants.SPRING_TEST_PROFILE)
class KafkaProducerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoSpyBean
    private KafkaProducerService kafkaProducerService;

    @Nested
    class PublishMessage {
        @Test
        void testValidPublishMessageReturnOK() throws Exception {
            final KafkaDTO message = new KafkaDTO(UUID.randomUUID(), "test", 123);
            mockMvc
                    .perform(post("/kafka/publish")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(message)))
                    .andExpect(status().isOk());
            verify(kafkaProducerService).publishMessage(message);
        }
    }
}
