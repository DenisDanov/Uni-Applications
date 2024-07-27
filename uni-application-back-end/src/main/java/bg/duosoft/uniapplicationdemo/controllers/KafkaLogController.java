package bg.duosoft.uniapplicationdemo.controllers;

import bg.duosoft.uniapplicationdemo.models.dtos.ApplicationLogEventDTO;
import bg.duosoft.uniapplicationdemo.services.impl.KafkaLogRetrieverService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/logs")
@RequiredArgsConstructor
public class KafkaLogController {

    private final KafkaLogRetrieverService logRetrieverService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_admin') and hasAnyRole('ROLE_READ_ONLY_ACCESS','ROLE_FULL_ACCESS')")
    public List<ApplicationLogEventDTO> getLogs() {
        List<ApplicationLogEventDTO> logs;

        try {
            // Retrieve logs and store them in a list
            logs = logRetrieverService.retrieveLogs();
        } catch (Exception e) {
            // Handle exceptions
            throw new RuntimeException("Failed to retrieve logs", e);
        }

        return logs;
    }
}
