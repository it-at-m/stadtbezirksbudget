package de.muenchen.stadtbezirksbudget.kafka;

import de.muenchen.stadtbezirksbudget.common.KafkaDTO;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "sbb-eai-topic", groupId = "sbb-backend")
    public void listen(@Header(name = KafkaHeaders.RECEIVED_KEY) String key, KafkaDTO content) {
        UUID uuidKey = UUID.fromString(key);
        LOG.info("Received message in group sbb-backend with key <" + uuidKey + ">: " + content.toString());
    }
}