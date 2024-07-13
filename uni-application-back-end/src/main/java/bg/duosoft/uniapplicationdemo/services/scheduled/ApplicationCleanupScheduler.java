package bg.duosoft.uniapplicationdemo.services.scheduled;

import bg.duosoft.uniapplicationdemo.models.entities.DeclinedStudentApplicationEntity;
import bg.duosoft.uniapplicationdemo.repositories.DeclinedStudentApplicationRepository;
import bg.duosoft.uniapplicationdemo.services.StudentApplicationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class ApplicationCleanupScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationCleanupScheduler.class);

    private final DeclinedStudentApplicationRepository declinedStudentApplicationRepository;

    private final StudentApplicationService studentApplicationService;

    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000) // Check every day
    public void checkForCleanup() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        logger.info("Checking for application cleanup");

        for (DeclinedStudentApplicationEntity entry : declinedStudentApplicationRepository.findAll()) {
            long daysSinceRejection = ChronoUnit.DAYS.between(entry.getDeleteDate(), now);

            // Delete if it's been 7 days since rejection
            if (daysSinceRejection >= 7) {
                studentApplicationService.deleteById(entry.getId().getUsername(), entry.getId().getFacultyId(), entry.getId().getSpecialtyId());
                logger.info("Application deleted after rejection.");
            }
        }
    }
}
