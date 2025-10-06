package de.muenchen.stadtbezirksbudget.backend.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "sbb-eai-topic", groupId = "sbb-backend")
    public void listen(String message) {
        LOG.info("Received message in group sbb-backend: " + message);
    }
}
