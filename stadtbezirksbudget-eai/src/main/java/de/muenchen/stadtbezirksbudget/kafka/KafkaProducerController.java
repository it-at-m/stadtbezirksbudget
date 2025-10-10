package de.muenchen.stadtbezirksbudget.kafka;

import de.muenchen.stadtbezirksbudget.common.KafkaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kafka")
public class KafkaProducerController {
    private final KafkaProducerService kafkaProducerService;

    private static final String TOPIC = "sbb-eai-topic";
    @Autowired
    private KafkaTemplate<String, KafkaDTO> kafkaTemplate;

    @PostMapping("/publish")
    public String publishMessage(@RequestBody KafkaDTO message) {
        kafkaProducerService.publishMessage(message);
        return "Message published successfully";
    }
}
