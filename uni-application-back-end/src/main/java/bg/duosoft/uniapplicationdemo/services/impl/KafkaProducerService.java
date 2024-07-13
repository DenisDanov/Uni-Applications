package bg.duosoft.uniapplicationdemo.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(String key, Object message) {
        kafkaTemplate.send("test-state" + key, key, message);
    }
}
