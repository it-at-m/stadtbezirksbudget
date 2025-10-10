package de.muenchen.stadtbezirksbudget.kafka;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, topics = { "sbb-eai-topic" })
class KafkaProducerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testPublishMessage() throws Exception {
        String message = "{\"id\":\"" + UUID.randomUUID() + "\",\"param1\":\"test\",\"param2\":123}";
        mockMvc.perform(post("/kafka/publish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(message))
                .andExpect(status().isOk());
    }
}
