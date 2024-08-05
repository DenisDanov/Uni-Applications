package bg.duosoft.uniapplicationdemo.services.impl;

import bg.duosoft.uniapplicationdemo.models.dtos.ApplicationLogEventDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.StudentApplicationDTO;
import bg.duosoft.uniapplicationdemo.services.LogEventsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(LogEventsServiceImpl.class);

    @Async("threadPoolTaskExecutorLogs")
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

        kafkaTemplate.send("applications_log", applicationLogEventDTO);
    }

    @Override
    @Async("threadPoolTaskExecutorLogs")
    public void logDeletionEvent(String username, String facultyName, String specialtyName, String deletedByUsername) {
        logger.info("Trying to log deletion event.");
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
        logger.info("Trying to log deletion event. X2");
        kafkaTemplate.send("applications_log", latestLog);
    }
}
