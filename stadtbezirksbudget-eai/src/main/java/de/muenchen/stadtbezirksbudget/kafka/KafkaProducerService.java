package de.muenchen.stadtbezirksbudget.kafka;

import de.muenchen.stadtbezirksbudget.common.KafkaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kafka")
public class KafkaProducerService {

    private static final String TOPIC = "sbb-eai-topic";
    private final KafkaTemplate<String, KafkaDTO> kafkaTemplate;

    public void publishMessage(KafkaDTO content) {
        kafkaTemplate.send(TOPIC, content.id().toString(), content);
    }
}
