package bg.duosoft.uniapplicationdemo.services.impl;

import bg.duosoft.uniapplicationdemo.models.dtos.TestStateDTOFinal;
import bg.duosoft.uniapplicationdemo.services.StudentsRequirementsResultsService;
import com.google.gson.Gson;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class TestStateService {

    private final StringRedisTemplate redisTemplate;

    private final String TEST_KEY_PREFIX = "test:";

    private static final Logger logger = LoggerFactory.getLogger(TestStateService.class);

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

    private final ConcurrentMap<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    private final Gson gson;

    private final StudentsRequirementsResultsService studentsRequirementsResultsService;

    @PreDestroy
    public void shutdown() {
        scheduler.shutdown();
    }

    public void saveTestState(String username, TestStateDTOFinal request) {
        String key = TEST_KEY_PREFIX + username;
        redisTemplate.opsForValue().set(key, gson.toJson(request));
        scheduleExpirationTask(username, request.getTestStartTime());
    }

    public String getTestState(String username) {
        String key = TEST_KEY_PREFIX + username;
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteTestState(String username) {
        logger.info("Deleting test state for user: {}", username);
        String key = TEST_KEY_PREFIX + username;
        boolean isDeleted = Boolean.TRUE.equals(redisTemplate.delete(key));
        logger.info(isDeleted ? "Successfully deleted test state for " + username : "Failed to delete test state for " + username);
        cancelScheduledTask(username);
    }

    private synchronized void scheduleExpirationTask(String username, long deadline) {
        ScheduledFuture<?> existingTask = scheduledTasks.get(username);
        if (existingTask == null || existingTask.isDone() || existingTask.isCancelled()) {
            // Calculate delay in milliseconds from the current time to the deadline
            long currentTime = Instant.now().getEpochSecond();
            long delayInSeconds = deadline - currentTime + 5;

            // Ensure the delay is not negative
            if (delayInSeconds < 0) {
                delayInSeconds = 0;
            }

            Runnable task = () -> {
                String key = TEST_KEY_PREFIX + username;
                String json = redisTemplate.opsForValue().get(key);
                studentsRequirementsResultsService.processTestResults(json);
                Boolean isDeleted = redisTemplate.delete(key);
                if (Boolean.TRUE.equals(isDeleted)) {
                    logger.info("Deleted test state for user: {}", username);
                } else {
                    logger.info("Expired key: {}, Value: Not found or already deleted", key);
                }
                cancelScheduledTask(username); // Ensure the task is removed after execution
            };

            ScheduledFuture<?> scheduledTask = scheduler.schedule(task, delayInSeconds, TimeUnit.SECONDS);
            scheduledTasks.put(username, scheduledTask);
            logger.info("Scheduled expiration task for user: {}", username);

            Instant executionTime = Instant.now().plusSeconds(delayInSeconds);
            String formattedExecutionTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.systemDefault())
                    .format(executionTime);
            logger.info("Will execute at: {}", formattedExecutionTime);
        }
    }

    private synchronized void cancelScheduledTask(String username) {
        ScheduledFuture<?> scheduledTask = scheduledTasks.remove(username);
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
            logger.info(scheduledTask.isCancelled() ? "Canceled expiration task for user: {}" : "Failed to cancel expiration task for user: {}", username);
        }
    }
}
