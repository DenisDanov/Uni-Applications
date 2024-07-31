package bg.duosoft.uniapplicationdemo.services.impl;

import bg.duosoft.uniapplicationdemo.models.dtos.ApplicationLogEventDTO;
import jakarta.annotation.PreDestroy;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class KafkaLogRetrieverService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaLogRetrieverService.class);

    private KafkaConsumer<String, ApplicationLogEventDTO> consumer;

    private final Properties baseConsumerProps;

    // Lock to synchronize access to the consumer
    private final Lock consumerLock = new ReentrantLock();

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

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
        consumerLock.lock();
        try {
            // Generate a new unique group ID for this retrieval
            String uniqueGroupId = "log-retriever-group-" + UUID.randomUUID();

            // Create new consumer properties with the unique group ID
            Properties consumerProps = new Properties();
            consumerProps.putAll(baseConsumerProps);
            consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, uniqueGroupId);

            // Create a new KafkaConsumer with the updated properties
            consumer = new KafkaConsumer<>(consumerProps);
            consumer.subscribe(Collections.singletonList("applications_log"));

            ConsumerRecords<String, ApplicationLogEventDTO> records = consumer.poll(Duration.ofMillis(1000));

            final KafkaConsumer<String, ApplicationLogEventDTO> finalConsumer = consumer;
            executorService.submit(() -> {
                try {
                    finalConsumer.close();
                    logger.info("Consumer closed.");
                } catch (Exception e) {
                    logger.error("Error occurred while closing Kafka consumer: {}", e.getMessage(), e);
                }
            });

            if (records.isEmpty()) {
                return new ArrayList<>();
            }

            // Collect all records into a list
            return records.records(new TopicPartition("applications_log", 0)) // Replace with correct partition handling
                    .stream()
                    .map(ConsumerRecord::value)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Error occurred while retrieving logs: {}", e.getMessage(), e);
            return new ArrayList<>();
        } finally {
            consumerLock.unlock();
        }
    }

    @PreDestroy
    private void close() {
        try {
            if (consumer != null) {
                consumer.close();
                logger.info("Kafka consumer closed.");
            }
        } catch (Exception e) {
            logger.error("Error occurred while closing Kafka resources: {}", e.getMessage(), e);
        }
    }
}
