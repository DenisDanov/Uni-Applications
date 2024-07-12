package bg.duosoft.uniapplicationdemo.models.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
public class ApplicationLogEventDTO {
    private String submittedByUsername;
    private String facultyName;
    private Long facultyId;
    private String specialtyName;
    private Long specialtyId;
    private LocalDateTime submissionTime;
    private String processedByUsername;
    private LocalDateTime processingTime;

    public ApplicationLogEventDTO(String submittedByUsername, String facultyName, Long facultyId, String specialtyName, Long specialtyId, LocalDateTime submissionTime) {
        this.submittedByUsername = submittedByUsername;
        this.facultyName = facultyName;
        this.facultyId = facultyId;
        this.specialtyName = specialtyName;
        this.specialtyId = specialtyId;
        this.submissionTime = submissionTime;
    }

    public ApplicationLogEventDTO(String submittedByUsername, String facultyName, Long facultyId, String specialtyName, Long specialtyId, LocalDateTime submissionTime, String processedByUsername, LocalDateTime processingTime) {
        this(submittedByUsername,facultyName,facultyId,specialtyName,specialtyId,submissionTime);
        this.processedByUsername = processedByUsername;
        this.processingTime = processingTime;
    }
}
