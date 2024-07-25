package bg.duosoft.uniapplicationdemo.models.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class ApplicationLogEventDTO {
    private String submittedByUsername;
    private String facultyName;
    private Long facultyId;
    private String specialtyName;
    private Long specialtyId;
    private LocalDateTime submissionTime;
    private String processedStatus;
    private String processedByUsername;
    private LocalDateTime processingTime;
    private String deletedByUsername;
    private LocalDateTime deletedTime;

    public ApplicationLogEventDTO(String submittedByUsername, String facultyName, Long facultyId, String specialtyName, Long specialtyId, LocalDateTime submissionTime) {
        this.submittedByUsername = submittedByUsername;
        this.facultyName = facultyName;
        this.facultyId = facultyId;
        this.specialtyName = specialtyName;
        this.specialtyId = specialtyId;
        this.submissionTime = submissionTime;
    }

    public ApplicationLogEventDTO(String submittedByUsername,
                                  String facultyName,
                                  Long facultyId,
                                  String specialtyName,
                                  Long specialtyId,
                                  LocalDateTime submissionTime,
                                  String processedByUsername,
                                  LocalDateTime processingTime,
                                  String processedStatus) {
        this(submittedByUsername,facultyName,facultyId,specialtyName,specialtyId,submissionTime);
        this.processedByUsername = processedByUsername;
        this.processingTime = processingTime;
        this.processedStatus = processedStatus;
    }
}
