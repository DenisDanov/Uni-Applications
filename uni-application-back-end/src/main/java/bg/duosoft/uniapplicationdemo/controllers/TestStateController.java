package bg.duosoft.uniapplicationdemo.controllers;

import bg.duosoft.uniapplicationdemo.services.impl.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestStateController {

    private final KafkaProducerService kafkaProducerService;

    @PostMapping("/save-test-state")
    public String saveTestState(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");

        kafkaProducerService.sendMessage(username, request);
        return "State saved";
    }
}
