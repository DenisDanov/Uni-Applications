package bg.duosoft.uniapplicationdemo.controllers;

import bg.duosoft.uniapplicationdemo.models.dtos.TestStateDTO;
import bg.duosoft.uniapplicationdemo.services.impl.TestStateService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestStateController {

    private final TestStateService testStateService;

    @PostMapping("/save-test-state")
    public String saveTestState(@RequestBody Map<String, Object> request) {
        testStateService.saveTestState((String) request.get("username"), request, (Integer) request.get("secondsLeft"));
        return "State saved";
    }

    @GetMapping("/get/{username}")
    public TestStateDTO getTestState(@PathVariable String username) {
        String stateJson = testStateService.getTestState(username);
        TestStateDTO testStateDTO = new Gson().fromJson(stateJson, TestStateDTO.class);
        return testStateDTO == null ? new TestStateDTO() : testStateDTO;
    }
}
