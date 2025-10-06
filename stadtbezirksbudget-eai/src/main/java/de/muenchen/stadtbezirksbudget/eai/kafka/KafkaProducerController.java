package de.muenchen.stadtbezirksbudget.eai.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
public class KafkaProducerController {

    private static final String TOPIC = "sbb-eai-topic";
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping("/publish")
    public String publishMessage(@RequestBody String message) {
        kafkaTemplate.send(TOPIC, message);
        return "Message published successfully";
    }
}
