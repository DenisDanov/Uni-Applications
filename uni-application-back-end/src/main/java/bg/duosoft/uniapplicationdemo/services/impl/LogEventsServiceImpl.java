package bg.duosoft.uniapplicationdemo.services.impl;

import bg.duosoft.uniapplicationdemo.models.dtos.ApplicationLogEventDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.StudentApplicationDTO;
import bg.duosoft.uniapplicationdemo.services.LogEventsService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class LogEventsServiceImpl implements LogEventsService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Async("threadPoolTaskExecutor")
    @Override
    public void logEvent(StudentApplicationDTO studentApplicationDTO, String facultyName, String specialtyName, String processedByUsername, LocalDateTime processingTime) {
        ApplicationLogEventDTO logEvent = new ApplicationLogEventDTO(
                studentApplicationDTO.getUsername(),
                facultyName,
                studentApplicationDTO.getFacultyId(),
                specialtyName,
                studentApplicationDTO.getSpecialtyId(),
                studentApplicationDTO.getApplicationSentDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                processedByUsername,
                processingTime
        );
        kafkaTemplate.send("applications_log", logEvent);
    }
}
