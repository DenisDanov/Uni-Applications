package bg.duosoft.uniapplicationdemo.controllers;

import bg.duosoft.uniapplicationdemo.services.impl.KafkaConsumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class KafkaController {

    private final KafkaConsumerService kafkaConsumerService;

    @GetMapping("/denko")
    public List<String> getMessagesWithDenkoKey() {
        return kafkaConsumerService.getMessages();
    }
}
