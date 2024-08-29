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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class KafkaLogRetrieverService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaLogRetrieverService.class);

    private final Properties baseConsumerProps;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

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
    }

    public List<ApplicationLogEventDTO> retrieveLogs() {
        List<ApplicationLogEventDTO> logs = new ArrayList<>();
        Consumer<String, ApplicationLogEventDTO> consumer = creatConsumer();
        try {
            ConsumerRecords<String, ApplicationLogEventDTO> records = consumer.poll(Duration.ofMillis(5000));

            return records.isEmpty() ? new ArrayList<>() : records.records(topicPartition) // Replace with correct partition handling
                    .stream()
                    .map(ConsumerRecord::value)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error occurred while retrieving logs: {}", e.getMessage(), e);
        } finally {
            executorService.submit(() -> {
                try {
                    consumer.close();
                    logger.info("Consumer closed.");
                } catch (Exception e) {
                    logger.error("Error occurred while closing Kafka consumer: {}", e.getMessage(), e);
                }
            });
        }
        return logs;
    }

    private Consumer<String, ApplicationLogEventDTO> creatConsumer() {
        String uniqueGroupId = "log-retriever-group-" + UUID.randomUUID();

        // Create new consumer properties with the unique group ID
        Properties consumerProps = new Properties();
        consumerProps.putAll(baseConsumerProps);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, uniqueGroupId);

        Consumer<String, ApplicationLogEventDTO> consumer = new KafkaConsumer<>(consumerProps);
        consumer.subscribe(Collections.singletonList("applications_log"));

        return consumer;
    }
}
