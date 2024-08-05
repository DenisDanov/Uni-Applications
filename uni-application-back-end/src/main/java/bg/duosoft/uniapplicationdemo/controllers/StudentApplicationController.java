package bg.duosoft.uniapplicationdemo.controllers;

import bg.duosoft.uniapplicationdemo.controllers.base.CrudController;
import bg.duosoft.uniapplicationdemo.models.dtos.FilterStudentApplicationsDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.StudentApplicationDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.StudentApplicationDTOUsers;
import bg.duosoft.uniapplicationdemo.models.entities.compositeKeys.StudentsApplicationsId;
import bg.duosoft.uniapplicationdemo.security.SecurityUtils;
import bg.duosoft.uniapplicationdemo.services.*;
import bg.duosoft.uniapplicationdemo.validators.config.ValidationErrorException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/student-application")
@RequiredArgsConstructor
public class StudentApplicationController extends CrudController<StudentsApplicationsId, StudentApplicationDTO, StudentApplicationService> {

    private final UserService userService;

    private final ApplicationStatusService applicationStatusService;

    private final EmailService emailService;

    private static final Logger logger = LoggerFactory.getLogger(StudentApplicationController.class);

    private final LogEventsService logEventsService;

    @PostMapping("/create-application")
    public ResponseEntity<?> createApplication(MultipartHttpServletRequest request) {
        Jwt principal = SecurityUtils.getJwt();
        try {
            StudentApplicationDTO studentApplicationCreateDTO = super.getService().createApplication(request, principal);
            return ResponseEntity.ok(studentApplicationCreateDTO);
        } catch (ValidationErrorException e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getErrors());
        } catch (IOException ex) {
            logger.error("Error parsing DTO JSON: {}", ex.getMessage());
            return ResponseEntity.badRequest().body("Error parsing DTO JSON: " + ex.getMessage());
        } catch (Exception ex) {
            logger.error("Error uploading file: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file: " + ex.getMessage());
        }
    }

    @GetMapping("/my-applications")
    @PreAuthorize("hasRole('ROLE_student')")
    public ResponseEntity<List<StudentApplicationDTOUsers>> getMyApplications() {
        Jwt principal = SecurityUtils.getJwt();
        String username = principal.getClaim("preferred_username");
        List<StudentApplicationDTOUsers> studentApplicationsByUsername = super.getService().findStudentApplicationsForUser(username);
        return ResponseEntity.ok(studentApplicationsByUsername);
    }

    @GetMapping({"/applications/{username}", "/applications/{username}/{facultyName}", "/applications/{username}/{facultyName}/{specialtyName}"})
    @PreAuthorize("hasRole('ROLE_admin') and hasAnyRole('ROLE_READ_ONLY_ACCESS','ROLE_FULL_ACCESS')")
    public ResponseEntity<List<StudentApplicationDTO>> getApplicationsById(
            @PathVariable("username") String username,
            @PathVariable(value = "facultyName", required = false) String facultyName,
            @PathVariable(value = "specialtyName", required = false) String specialtyName) {

        if (userService.getById(username) == null) {
            return ResponseEntity.status(404).build();
        } else {
            return ResponseEntity.ok(super.getService().findByStudentUsernameAndFacultyAndSpecialty(username, facultyName, specialtyName));
        }
    }

    @PostMapping("/applications")
    @PreAuthorize("hasRole('ROLE_admin') and hasAnyRole('ROLE_READ_ONLY_ACCESS','ROLE_FULL_ACCESS')")
    public ResponseEntity<List<StudentApplicationDTOUsers>> getApplications(@RequestBody FilterStudentApplicationsDTO filter) {
        if (!Objects.isNull(filter)) {
            return ResponseEntity.ok(super.getService().filterStudentApplications(filter));
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    @PutMapping("/applications/{status}")
    @PreAuthorize("hasRole('ROLE_admin') and hasRole('ROLE_FULL_ACCESS')")
    public ResponseEntity<StudentApplicationDTOUsers> processStudentApplication(@PathVariable String status,
                                                                                @RequestParam(value = "username") String username,
                                                                                @RequestParam(value = "faculty") String facultyName,
                                                                                @RequestParam(value = "specialty") String specialtyName) {
        if (status.equals("accept") || status.equals("decline")) {
            StudentApplicationDTOUsers studentApplicationDTO = super.getService().findByStudentsApplicationsIdAndStatus(username, facultyName, specialtyName);
            if (studentApplicationDTO == null) {
                return ResponseEntity.status(404).build();
            } else {
                if (status.equals("accept")) {
                    studentApplicationDTO.setApplicationStatus(applicationStatusService.getById("ACCEPTED"));
                    logger.info("Application accepted");
                    emailService.sendEmailAsync(userService.findByUsername(studentApplicationDTO.getUsername()).get().getEmail(), "Application accepted",
                            "Congratulations ! Your application for %s in %s is accepted.".formatted(specialtyName, facultyName));
                } else {
                    studentApplicationDTO.setApplicationStatus(applicationStatusService.getById("DECLINED"));
                    logger.info("Application declined.");
                }
                super.getService().update(studentApplicationDTO);
                logger.info("Returning response");
                logEventsService.logEvent(studentApplicationDTO,
                        studentApplicationDTO.getFacultyName(),
                        studentApplicationDTO.getSpecialtyName(),
                        SecurityUtils.getJwt().getClaim("preferred_username"),
                        LocalDateTime.now(),
                        studentApplicationDTO.getApplicationStatus().getApplicationStatus());
                return ResponseEntity.ok(studentApplicationDTO);
            }
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping("/applications/delete")
    @PreAuthorize("hasRole('ROLE_admin') and hasRole('ROLE_FULL_ACCESS')")
    public ResponseEntity<Object> deleteStudentApplication(@RequestParam(value = "username") String username,
                                                           @RequestParam(value = "faculty") String facultyName,
                                                           @RequestParam(value = "specialty") String specialtyName) throws Exception {
        if (super.getService().deleteByUsernameAndAndFacultyAndSpecialty(username, facultyName, specialtyName) == 1) {
            logEventsService.logDeletionEvent(username, facultyName.split("-")[0], specialtyName.split("-")[0], SecurityUtils.getJwt().getClaim("preferred_username"));
            return ResponseEntity.ok("Deleted successfully.");
        } else {
            return ResponseEntity.status(404).build();
        }
    }
}
