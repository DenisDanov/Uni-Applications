package bg.duosoft.uniapplicationdemo.services.impl;

import bg.duosoft.uniapplicationdemo.mappers.StudentApplicationDTOUsersMapper;
import bg.duosoft.uniapplicationdemo.mappers.StudentApplicationMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.FilterStudentApplicationsDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.StudentApplicationDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.StudentApplicationDTOUsers;
import bg.duosoft.uniapplicationdemo.models.entities.StudentApplicationEntity;
import bg.duosoft.uniapplicationdemo.models.entities.compositeKeys.StudentsApplicationsId;
import bg.duosoft.uniapplicationdemo.repositories.StudentApplicationRepository;
import bg.duosoft.uniapplicationdemo.repositories.custom.StudentApplicationRepositoryCustom;
import bg.duosoft.uniapplicationdemo.services.LogEventsService;
import bg.duosoft.uniapplicationdemo.services.StudentApplicationService;
import bg.duosoft.uniapplicationdemo.services.base.BaseServiceImpl;
import bg.duosoft.uniapplicationdemo.validators.StudentApplicationValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentApplicationServiceImpl extends BaseServiceImpl<StudentsApplicationsId, StudentApplicationDTO, StudentApplicationEntity, StudentApplicationMapper, StudentApplicationValidator, StudentApplicationRepository> implements StudentApplicationService {

    private final StudentApplicationRepositoryCustom studentApplicationRepositoryCustom;

    private final StudentApplicationDTOUsersMapper studentApplicationDTOUsersMapper;

    private final MinioService minioService;

    private static final Logger logger = LoggerFactory.getLogger(StudentApplicationServiceImpl.class);

    private final LogEventsService logEventsService;

    @Override
    public List<StudentApplicationDTO> findStudentApplicationsByUsername(String username) {
        return super.getMapper().toDtoList(super.getRepository().findStudentApplicationEntitiesByUsername(username));
    }

    @Override
    public List<StudentApplicationDTOUsers> findStudentApplicationsForUser(String username) {
        logger.info("Getting applications");
        List<StudentApplicationDTOUsers> applications = studentApplicationDTOUsersMapper.toDtoList(super.getRepository().findStudentApplicationEntitiesByUsername(username));
        applications.forEach(application -> {
            try {
                application.setApplicationFiles(minioService.listFiles(application.getUsername() + " - Specialty - " + application.getSpecialtyId() + " - Faculty - " + application.getFacultyId()));
                logger.info("Got application");
            } catch (Exception e) {
                logger.debug("Error getting application files.");
                logger.error(e.getMessage());
            }
        });
        return applications;
    }

    @Override
    public List<StudentApplicationDTO> findByStudentUsernameAndFacultyAndSpecialty(String studentUsername, String facultyName, String specialtyName) {
        if (facultyName == null && specialtyName == null) {
            return super.getMapper().toDtoList(super.getRepository().findStudentApplicationEntitiesByUsername(studentUsername));
        } else if (specialtyName == null) {
            return super.getMapper().toDtoList(super.getRepository().findStudentApplicationEntitiesByUsernameAndFacultyFacultyName(studentUsername, facultyName));
        } else {
            return super.getRepository().findByStudentsApplicationsId(studentUsername, facultyName, specialtyName).map
                    (studentApplicationEntity -> List.of(super.getMapper().toDto(studentApplicationEntity))).orElseGet(ArrayList::new);
        }
    }

    @Override
    public List<StudentApplicationDTO> findByFacultyAndSpecialty(String faculty, String specialty) {
        return super.getMapper().toDtoList(super.getRepository().findStudentApplicationEntitiesByFacultyFacultyNameAndSpecialtySpecialtyName(faculty, specialty));
    }

    @Override
    public List<StudentApplicationDTO> findByFaculty(String faculty) {
        return super.getMapper().toDtoList(super.getRepository().findStudentApplicationEntitiesByFacultyFacultyName(faculty));
    }

    @Override
    public List<StudentApplicationDTO> findBySpecialty(String specialty) {
        return super.getMapper().toDtoList(super.getRepository().findStudentApplicationEntitiesBySpecialtySpecialtyName(specialty));
    }

    @Override
    public StudentApplicationDTOUsers findByStudentsApplicationsIdAndStatus(String studentUsername, String facultyName, String specialtyName) {
        return super.getRepository().findByStudentsApplicationsIdAndStatus(studentUsername, facultyName, specialtyName).
                map(studentApplicationDTOUsersMapper::toDto).orElse(null);
    }

    @Override
    public StudentApplicationDTO findByStudentsApplicationsIdAndStatusAccepted(String studentUsername, String facultyName, String specialtyName) {
        return super.getRepository().findByStudentsApplicationsIdAndStatusAccepted
                (specialtyName, studentUsername).map(studentApplicationEntity -> super.getMapper().toDto(studentApplicationEntity)).orElse(null);
    }

    @Override
    public List<StudentApplicationDTOUsers> filterStudentApplications(FilterStudentApplicationsDTO filter) {
        List<StudentApplicationDTOUsers> dtoList = studentApplicationDTOUsersMapper.toDtoList(studentApplicationRepositoryCustom.searchRecords(filter));
        dtoList.forEach(studentApplicationDTOUsers -> {
            if (studentApplicationDTOUsers.getUsername() != null && studentApplicationDTOUsers.getFacultyId() != null && studentApplicationDTOUsers.getSpecialtyId() != null) {
                try {
                    studentApplicationDTOUsers.setApplicationFiles(minioService.listFiles("%s - Specialty - %d - Faculty - %d".formatted(
                            studentApplicationDTOUsers.getUsername(),
                            studentApplicationDTOUsers.getSpecialtyId(),
                            studentApplicationDTOUsers.getFacultyId())));
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        });
        return dtoList;
    }

    @Override
    public int deleteByUsernameAndAndFacultyAndSpecialty(String username, String facultyName, String specialtyName) throws Exception {
        int deletedApps = super.getRepository().deleteByUsernameAndAndFacultyAndSpecialty(username, facultyName.split("-")[0], specialtyName.split("-")[0]);
        if (deletedApps == 1) {
            minioService.deleteFolderByFullName("%s - Specialty - %d - Faculty - %d".formatted(username, Integer.parseInt(specialtyName.split("-")[1]), Integer.parseInt(facultyName.split("-")[1])));
        }
        return deletedApps;
    }

    @Override
    public int deleteById(String username, Long facultyId, Long specialtyId) throws Exception {
        super.getRepository().deleteById(new StudentsApplicationsId(username, facultyId, specialtyId));
        minioService.deleteFolderByFullName("%s - Specialty - %d - Faculty - %d".formatted(username, specialtyId, facultyId));
        return 1;
    }

    @Override
    public void deleteByUsername(String username) throws Exception {
        int deletedApps = super.getRepository().deleteByUsername(username);
        if (deletedApps >= 1) {
            minioService.deleteFolderByUsernamePrefix(username);
        }
    }

    @Override
    public List<StudentApplicationDTO> findDeclinedApplications() {
        return super.getMapper().toDtoList(super.getRepository().findDeclinedApplications());
    }

    @Override
    public boolean isCachingEnabled() {
        return false;
    }

    @Override
    public StudentApplicationDTO createApplication(MultipartHttpServletRequest request, Jwt principal)
            throws Exception {
        // Get the JSON string of DTO from the request
        String dtoJsonString = request.getParameter("studentApplicationCreateDTO");

        // Convert the JSON string to DTO
        ObjectMapper objectMapper = new ObjectMapper();
        StudentApplicationDTO studentApplicationCreateDTO = objectMapper.readValue(dtoJsonString, StudentApplicationDTO.class);
        studentApplicationCreateDTO.setUsername(principal.getClaim("preferred_username"));

        this.create(studentApplicationCreateDTO);

        // Iterate through the files and upload each one
        Iterator<String> fileNames = request.getFileNames();
        while (fileNames.hasNext()) {
            String fileName = fileNames.next();
            List<MultipartFile> files = request.getFiles(fileName);
            for (MultipartFile file : files) {
                if (file != null) {
                    try {
                        minioService.uploadFile(file, principal.getClaim("preferred_username") +
                                " - Specialty - " + studentApplicationCreateDTO.getSpecialtyId() +
                                " - Faculty - " + studentApplicationCreateDTO.getFacultyId());
                    } catch (Exception e) {
                        this.deleteById(principal.getClaim("preferred_username"), studentApplicationCreateDTO.getFacultyId(), studentApplicationCreateDTO.getSpecialtyId());
                        throw new Exception(e.getMessage());
                    }
                }
            }
        }

        logEventsService.logEvent(
                studentApplicationCreateDTO,
                request.getParameter("facultyName"),
                request.getParameter("specialtyName"),
                null,
                null);

        return studentApplicationCreateDTO;
    }
}
