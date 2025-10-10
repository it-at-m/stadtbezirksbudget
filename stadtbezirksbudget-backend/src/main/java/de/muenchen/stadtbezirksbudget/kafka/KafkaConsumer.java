package de.muenchen.stadtbezirksbudget.kafka;

import de.muenchen.stadtbezirksbudget.common.KafkaDTO;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumer {
    @KafkaListener(topics = "sbb-eai-topic", groupId = "sbb-backend")
    public void listen(@Header(name = KafkaHeaders.RECEIVED_KEY) String key, KafkaDTO content) {
        UUID uuidKey = UUID.fromString(key);
        log.info("Received message in group sbb-backend with key {}: {}", uuidKey, content.toString());
    }
}