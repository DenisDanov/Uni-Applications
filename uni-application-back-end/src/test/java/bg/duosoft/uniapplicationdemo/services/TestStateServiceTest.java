package bg.duosoft.uniapplicationdemo.services;

import bg.duosoft.uniapplicationdemo.models.dtos.TestStateDTOFinal;
import bg.duosoft.uniapplicationdemo.services.StudentsRequirementsResultsService;
import bg.duosoft.uniapplicationdemo.services.impl.TestStateService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestStateServiceTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private StudentsRequirementsResultsService studentsRequirementsResultsService;

    @Mock
    private Gson gson;

    @Mock
    private ScheduledFuture<?> mockScheduledFuture;

    @Mock
    private ScheduledExecutorService mockScheduler = Executors.newScheduledThreadPool(10);

    private TestStateService testStateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        testStateService = new TestStateService(
                redisTemplate,
                gson,
                studentsRequirementsResultsService
        );
    }

    @Test
    void testSaveTestState() {
        String username = "testUser";
        TestStateDTOFinal request = new TestStateDTOFinal();
        String json = "{}";

        when(gson.toJson(request)).thenReturn(json);

        testStateService.saveTestState(username, request);

        verify(valueOperations).set("test:" + username, json);
    }

    @Test
    void testGetTestState() {
        String username = "testUser";
        String json = "{}";
        when(valueOperations.get("test:" + username)).thenReturn(json);

        String result = testStateService.getTestState(username);

        assertEquals(json, result);
    }

    @Test
    void testDeleteTestState() {
        String username = "testUser";
        String key = "test:" + username;
        when(redisTemplate.delete(key)).thenReturn(true);

        testStateService.deleteTestState(username);

        verify(redisTemplate).delete(key);
        verify(studentsRequirementsResultsService, never()).processTestResults(anyString());
    }

    @Test
    void testCancelScheduledTask() throws NoSuchFieldException, IllegalAccessException {
        String username = "testUser";

        //reflection to access the private field
        Field scheduledTasksField = TestStateService.class.getDeclaredField("scheduledTasks");
        scheduledTasksField.setAccessible(true);

        // concurrentMap from the TestStateService instance
        @SuppressWarnings("unchecked")
        ConcurrentMap<String, ScheduledFuture<?>> scheduledTasks = (ConcurrentMap<String, ScheduledFuture<?>>) scheduledTasksField.get(testStateService);

        //mock ScheduledFuture and put it into the map
        when(mockScheduledFuture.isCancelled()).thenReturn(true);
        scheduledTasks.put(username, mockScheduledFuture);

        // call the method under test
        testStateService.deleteTestState(username);

        // verify the interactions
        verify(mockScheduledFuture).cancel(true);
        verify(redisTemplate).delete("test:" + username);
    }

    @Test
    void testShutdown() throws NoSuchFieldException, IllegalAccessException {
        Field schedulerField = TestStateService.class.getDeclaredField("scheduler");
        schedulerField.setAccessible(true);
        schedulerField.set(testStateService, mockScheduler);

        // Perform the shutdown
        testStateService.shutdown();

        // Verify the shutdown method is called on the mocked scheduler
        verify(mockScheduler).shutdown();
    }
}
