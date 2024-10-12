package bg.duosoft.uniapplicationdemo.services.impl;

import bg.duosoft.uniapplicationdemo.models.dtos.ApplicationLogEventDTO;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Service
public class KafkaLogRetrieverService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaLogRetrieverService.class);

    private final Properties baseConsumerProps;

    private final TopicPartition topicPartition = new TopicPartition("applications_log", 0);

    public KafkaLogRetrieverService() {
        // Configure base properties
        baseConsumerProps = new Properties();
        baseConsumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        baseConsumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        baseConsumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        baseConsumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        baseConsumerProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, ApplicationLogEventDTO.class.getName());
        baseConsumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        baseConsumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        baseConsumerProps.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, "1024");
        baseConsumerProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "500");
    }

    public List<ApplicationLogEventDTO> retrieveLogs() {
        List<ApplicationLogEventDTO> logs = new ArrayList<>();
        try (Consumer<String, ApplicationLogEventDTO> consumer = createConsumer()) {
            while (true) {
                ConsumerRecords<String, ApplicationLogEventDTO> records = consumer.poll(Duration.ofMillis(100));
                if (!records.isEmpty()) {
                    logs.addAll(records.records(topicPartition)
                            .stream()
                            .map(ConsumerRecord::value)
                            .toList());
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            logger.error("Error occurred while retrieving logs: {}", e.getMessage(), e);
        } finally {
            logger.info("Consumer closed.");
        }
        return logs;
    }

    private Consumer<String, ApplicationLogEventDTO> createConsumer() {
        String staticGroupId = "log-retriever-group";

        // Create new consumer properties with a static group ID
        Properties consumerProps = new Properties();
        consumerProps.putAll(baseConsumerProps);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, staticGroupId);

        Consumer<String, ApplicationLogEventDTO> consumer = new KafkaConsumer<>(consumerProps);
        consumer.subscribe(Collections.singletonList("applications_log"));

        return consumer;
    }
}
