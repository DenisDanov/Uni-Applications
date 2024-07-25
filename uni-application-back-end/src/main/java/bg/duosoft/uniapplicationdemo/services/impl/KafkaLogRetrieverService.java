package bg.duosoft.uniapplicationdemo.services.impl;

import bg.duosoft.uniapplicationdemo.models.dtos.ApplicationLogEventDTO;
import jakarta.annotation.PreDestroy;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.DeleteConsumerGroupsResult;
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

@Service
public class KafkaLogRetrieverService {
    private final KafkaConsumer<String, ApplicationLogEventDTO> consumer;
    private static final Logger logger = LoggerFactory.getLogger(KafkaLogRetrieverService.class);
    private final AdminClient adminClient;
    private final String groupId = "log-retriever-group";

    public KafkaLogRetrieverService() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, ApplicationLogEventDTO.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        consumer = new KafkaConsumer<>(props);

        Properties adminProps = new Properties();
        adminProps.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        adminClient = AdminClient.create(adminProps);

        consumer.subscribe(Collections.singletonList("applications_log"));
    }

    public List<ApplicationLogEventDTO> retrieveLogs() {

        List<ApplicationLogEventDTO> logs = new ArrayList<>();

        // Poll to assign partitions and reset offsets
        consumer.poll(Duration.ofMillis(100));
        Set<TopicPartition> partitions = new HashSet<>(consumer.assignment());
        consumer.seekToBeginning(partitions); // Reset offsets to the beginning

        while (true) {
            ConsumerRecords<String, ApplicationLogEventDTO> records = consumer.poll(Duration.ofMillis(1000));
            if (records.isEmpty()) {
                break; // Exit if no more records
            }

            for (ConsumerRecord<String, ApplicationLogEventDTO> record : records) {
                logs.add(record.value()); // Collect records into the list
            }
        }

        return logs;
    }

    public void cleanUpConsumerGroup() {
        try {
            // Delete consumer group offsets to force a reset
            DeleteConsumerGroupsResult result = adminClient.deleteConsumerGroups(Collections.singleton(groupId));
            result.all().get(); // Wait for the result
            logger.info("Consumer group '{}' has been deleted.", groupId);
        } catch (Exception e) {
            logger.error("Failed to delete consumer group '{}': {}", groupId, e.getMessage(), e);
        }
    }

    @PreDestroy
    private void close() {
        try {
            if (consumer != null) {
                consumer.close();
                logger.info("Kafka consumer closed.");
            }
            if (adminClient != null) {
                adminClient.close();
                logger.info("AdminClient closed.");
            }
        } catch (Exception e) {
            logger.error("Error occurred while closing Kafka resources: {}", e.getMessage(), e);
        }
    }
}
