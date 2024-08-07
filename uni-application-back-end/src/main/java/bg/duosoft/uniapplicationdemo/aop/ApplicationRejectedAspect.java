package bg.duosoft.uniapplicationdemo.aop;

import bg.duosoft.uniapplicationdemo.mappers.DeclinedStudentApplicationMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.DeclinedStudentApplicationDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.StudentApplicationDTOUsers;
import bg.duosoft.uniapplicationdemo.models.entities.DeclinedStudentApplicationEntity;
import bg.duosoft.uniapplicationdemo.repositories.DeclinedStudentApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Aspect
@Component
@RequiredArgsConstructor
public class ApplicationRejectedAspect {

    private final DeclinedStudentApplicationRepository declinedStudentApplicationRepository;

    private final DeclinedStudentApplicationMapper declinedStudentApplicationMapper;

    @Pointcut(value = "execution(* bg.duosoft.uniapplicationdemo.controllers.StudentApplicationController.processStudentApplication(..)) " +
            "&& args(status, username, facultyName, specialtyName)", argNames = "status,username,facultyName,specialtyName")
    public void applicationProcessed(String status, String username, String facultyName, String specialtyName) {
    }

    @AfterReturning(pointcut = "execution(* bg.duosoft.uniapplicationdemo.controllers.StudentApplicationController.processStudentApplication(..)) ", returning = "result")
    public void afterReturningProcessStudentApplication(JoinPoint joinPoint, ResponseEntity<StudentApplicationDTOUsers> result) {
        if (result.getBody() != null && "DECLINED".equals(result.getBody().getApplicationStatus().getApplicationStatus())) {
            StudentApplicationDTOUsers studentApplicationDTO = result.getBody();
            // Schedule cleanup after 7 days
            DeclinedStudentApplicationDTO declinedStudentApplication = new DeclinedStudentApplicationDTO(
                    studentApplicationDTO.getUsername(), studentApplicationDTO.getFacultyId(), studentApplicationDTO.getSpecialtyId(),
                    LocalDate.now().plusDays(7));
            DeclinedStudentApplicationEntity entity = declinedStudentApplicationMapper.toEntity(declinedStudentApplication);
            declinedStudentApplicationRepository.saveEntity(entity);
        }
    }

}
