package bg.duosoft.uniapplicationdemo.services;

import bg.duosoft.uniapplicationdemo.models.dtos.FilterStudentApplicationsDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.StudentApplicationDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.StudentApplicationDTOUsers;
import bg.duosoft.uniapplicationdemo.models.entities.compositeKeys.StudentsApplicationsId;
import bg.duosoft.uniapplicationdemo.services.base.BaseService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

public interface StudentApplicationService extends BaseService<StudentsApplicationsId, StudentApplicationDTO> {
    List<StudentApplicationDTO> findStudentApplicationsByUsername(String username);

    List<StudentApplicationDTOUsers> findStudentApplicationsForUser(String username);

    List<StudentApplicationDTO> findByStudentUsernameAndFacultyAndSpecialty(String studentUsername, String facultyName, String specialtyName);

    List<StudentApplicationDTO> findByFacultyAndSpecialty(String faculty, String specialty);

    List<StudentApplicationDTO> findByFaculty(String faculty);

    List<StudentApplicationDTO> findBySpecialty(String specialty);

    StudentApplicationDTOUsers findByStudentsApplicationsIdAndStatus(String studentUsername, String facultyName, String specialtyName);

    StudentApplicationDTO findByStudentsApplicationsIdAndStatusAccepted(String studentUsername, String facultyName, String specialtyName);

    List<StudentApplicationDTOUsers> filterStudentApplications(FilterStudentApplicationsDTO filter);

    int deleteByUsernameAndAndFacultyAndSpecialty(String username, String facultyName, String specialtyName) throws Exception;

    int deleteById(String username, Long facultyId, Long specialtyId) throws Exception;

    void deleteByUsername(String username) throws Exception;

    List<StudentApplicationDTO> findDeclinedApplications();

    StudentApplicationDTO createApplication(MultipartHttpServletRequest request, Jwt principal) throws Exception;
}
