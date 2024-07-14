package bg.duosoft.uniapplicationdemo.services.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor()
public class TestStateService {

    private final StringRedisTemplate redisTemplate;

    private final String TEST_KEY_PREFIX = "test:";

    private static final Logger logger = LoggerFactory.getLogger(TestStateService.class);

    public void saveTestState(String username, Map<String, Object> request, long expirationTime) {
        String key = TEST_KEY_PREFIX + username;
        redisTemplate.opsForValue().set(key, request.toString(), expirationTime, TimeUnit.SECONDS);
    }

    public String getTestState(String username) {
        String key = TEST_KEY_PREFIX + username;
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteTestState(String username) {
        logger.info("Deleting test state for user: {}", username);
        String key = TEST_KEY_PREFIX + username;
        logger.info(Boolean.TRUE.equals(redisTemplate.delete(key)) ? "Successfully deleted test state for " + username : "Failed to delete test state for " + username);
    }
}
