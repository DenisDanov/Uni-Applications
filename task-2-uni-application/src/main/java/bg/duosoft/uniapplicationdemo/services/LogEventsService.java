package bg.duosoft.uniapplicationdemo.services;

import bg.duosoft.uniapplicationdemo.models.dtos.StudentApplicationDTO;

import java.time.LocalDateTime;

public interface LogEventsService {
    void logEvent(StudentApplicationDTO studentApplicationCreateDTO, String facultyName, String specialtyName, String processedByUsername, LocalDateTime processingTime);
}
