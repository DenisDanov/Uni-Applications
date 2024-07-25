package bg.duosoft.uniapplicationdemo.services.impl;

import bg.duosoft.uniapplicationdemo.models.dtos.ApplicationLogEventDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.StudentApplicationDTO;
import bg.duosoft.uniapplicationdemo.services.LogEventsService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogEventsServiceImpl implements LogEventsService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final KafkaLogRetrieverService kafkaLogRetrieverService;

    @Async("threadPoolTaskExecutor")
    @Override
    public void logEvent(StudentApplicationDTO studentApplicationDTO, String facultyName, String specialtyName, String processedByUsername, LocalDateTime processingTime, String processStatus) {
        ApplicationLogEventDTO applicationLogEventDTO;

        if (processedByUsername != null) {
            List<ApplicationLogEventDTO> applicationLogEventDTOS = kafkaLogRetrieverService.retrieveLogs();

            List<ApplicationLogEventDTO> filteredLogs = applicationLogEventDTOS.stream()
                    .filter(log -> studentApplicationDTO.getUsername().equals(log.getSubmittedByUsername()))
                    .filter(log -> facultyName.equals(log.getFacultyName()))
                    .filter(log -> specialtyName.equals(log.getSpecialtyName()))
                    .toList();

            if (filteredLogs.isEmpty()) {
                return; // Exit if no matching logs are found
            }

            ApplicationLogEventDTO latestLog = filteredLogs.getLast();

            latestLog.setProcessedByUsername(processedByUsername);
            latestLog.setProcessingTime(processingTime);
            latestLog.setProcessedStatus(processStatus);

            applicationLogEventDTO = latestLog;
        } else {
            applicationLogEventDTO = new ApplicationLogEventDTO(studentApplicationDTO.getUsername(),
                    facultyName,
                    studentApplicationDTO.getFacultyId(),
                    specialtyName,
                    studentApplicationDTO.getSpecialtyId(),
                    LocalDateTime.now());
        }

        kafkaTemplate.send("applications_log", "%s-%s-%s".formatted(processedByUsername, facultyName, specialtyName), applicationLogEventDTO);
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public void logDeletionEvent(String username, String facultyName, String specialtyName, String deletedByUsername) {
        List<ApplicationLogEventDTO> applicationLogEventDTOS = kafkaLogRetrieverService.retrieveLogs();

        List<ApplicationLogEventDTO> filteredLogs = applicationLogEventDTOS.stream()
                .filter(log -> username.equals(log.getSubmittedByUsername()))
                .filter(log -> facultyName.equals(log.getFacultyName()))
                .filter(log -> specialtyName.equals(log.getSpecialtyName()))
                .toList();

        if (filteredLogs.isEmpty()) {
            return; // Exit if no matching logs are found
        }

        // Find the latest submissionTime and then the latest processingTime
        ApplicationLogEventDTO latestLog = filteredLogs.getLast();

        // Update the deletedTime property of the latest log
        latestLog.setDeletedTime(LocalDateTime.now());
        latestLog.setDeletedByUsername(deletedByUsername);

        // Send the updated log back to Kafka
        kafkaTemplate.send("applications_log", "%s-%s-%s".formatted(latestLog.getSubmittedByUsername(), facultyName, specialtyName), latestLog);
    }
}
