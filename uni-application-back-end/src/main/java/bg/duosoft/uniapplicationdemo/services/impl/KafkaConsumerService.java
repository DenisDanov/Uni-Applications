package bg.duosoft.uniapplicationdemo.services.impl;

import lombok.Getter;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
public class KafkaConsumerService {

    @Autowired
    private KafkaConsumer<String, String> kafkaConsumer;

    private final List<String> messages = new ArrayList<>();

    @KafkaListener(topics = "test-state-denko", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(ConsumerRecord<String, String> record) {
        if ("denko".equals(record.key())) {
            messages.add(record.value());
        }
    }
}
