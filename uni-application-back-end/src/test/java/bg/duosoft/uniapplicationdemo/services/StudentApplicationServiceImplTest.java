package bg.duosoft.uniapplicationdemo.services;

import bg.duosoft.uniapplicationdemo.mappers.StudentApplicationDTOUsersMapper;
import bg.duosoft.uniapplicationdemo.mappers.StudentApplicationMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.FilterStudentApplicationsDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.StudentApplicationDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.StudentApplicationDTOUsers;
import bg.duosoft.uniapplicationdemo.models.entities.StudentApplicationEntity;
import bg.duosoft.uniapplicationdemo.models.entities.compositeKeys.StudentsApplicationsId;
import bg.duosoft.uniapplicationdemo.repositories.StudentApplicationRepository;
import bg.duosoft.uniapplicationdemo.repositories.custom.StudentApplicationRepositoryCustom;
import bg.duosoft.uniapplicationdemo.services.impl.MinioService;
import bg.duosoft.uniapplicationdemo.services.impl.StudentApplicationServiceImpl;
import bg.duosoft.uniapplicationdemo.validators.StudentApplicationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.*;

import static com.mysql.cj.conf.PropertyKey.logger;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class StudentApplicationServiceImplTest {

    @Mock
    private StudentApplicationRepository repository;

    @Mock
    private StudentApplicationRepositoryCustom repositoryCustom;

    @Mock
    private StudentApplicationDTOUsersMapper dtoUsersMapper;

    @Mock
    private StudentApplicationMapper mapper;

    @Mock
    private MinioService minioService;

    @Mock
    private LogEventsService logEventsService;

    @Mock
    private StudentApplicationValidator validator;

    @InjectMocks
    private StudentApplicationServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.service = new StudentApplicationServiceImpl(repositoryCustom,dtoUsersMapper,minioService,logEventsService);
        this.service.setRepository(repository);
        this.service.setMapper(mapper);
        this.service.setValidator(validator);
    }

    @Test
    void testFindStudentApplicationsForUser_Success() throws Exception {
        // Prepare test data
        String username = "testUser";
        StudentApplicationEntity entity = new StudentApplicationEntity();
        StudentApplicationDTOUsers dtoUser = new StudentApplicationDTOUsers();
        dtoUser.setUsername(username);
        dtoUser.setSpecialtyId(1L);
        dtoUser.setFacultyId(1L);

        List<StudentApplicationEntity> entities = List.of(entity);
        List<StudentApplicationDTOUsers> dtos = List.of(dtoUser);

        // Mock repository and mapper
        when(repository.findStudentApplicationEntitiesByUsername(username)).thenReturn(entities);
        when(dtoUsersMapper.toDtoList(entities)).thenReturn(dtos);
        when(minioService.listFiles(anyString())).thenReturn(new ArrayList<>());

        // Call method under test
        List<StudentApplicationDTOUsers> result = service.findStudentApplicationsForUser(username);

        // Verify results and interactions
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(dtoUser, result.get(0));
        verify(minioService).listFiles("testUser - Specialty - 1 - Faculty - 1");
    }

    @Test
    void testFindStudentApplicationsForUser_ExceptionHandling() throws Exception {
        // Prepare test data
        String username = "testUser";
        StudentApplicationEntity entity = new StudentApplicationEntity();
        StudentApplicationDTOUsers dtoUser = new StudentApplicationDTOUsers();
        dtoUser.setUsername(username);
        dtoUser.setSpecialtyId(1L);
        dtoUser.setFacultyId(1L);

        List<StudentApplicationEntity> entities = List.of(entity);
        List<StudentApplicationDTOUsers> dtos = List.of(dtoUser);

        // Mock repository and mapper
        when(repository.findStudentApplicationEntitiesByUsername(username)).thenReturn(entities);
        when(dtoUsersMapper.toDtoList(entities)).thenReturn(dtos);
        when(minioService.listFiles(anyString())).thenThrow(new RuntimeException("Test exception"));

        // Call method under test
        List<StudentApplicationDTOUsers> result = service.findStudentApplicationsForUser(username);

        // Verify results and interactions
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(dtoUser, result.get(0));
        verify(minioService).listFiles("testUser - Specialty - 1 - Faculty - 1");
    }

    @Test
    void testFindStudentApplicationsByUsername() {
        String username = "testUser";
        List<StudentApplicationEntity> entities = new ArrayList<>();
        List<StudentApplicationDTO> dtos = new ArrayList<>();
        when(repository.findStudentApplicationEntitiesByUsername(username)).thenReturn(entities);
        when(mapper.toDtoList(entities)).thenReturn(dtos);

        List<StudentApplicationDTO> result = service.findStudentApplicationsByUsername(username);

        assertEquals(dtos, result);
        verify(repository).findStudentApplicationEntitiesByUsername(username);
        verify(mapper).toDtoList(entities);
    }

    @Test
    void testFindStudentApplicationsForUser() throws Exception {
        String username = "testUser";
        List<StudentApplicationEntity> entities = new ArrayList<>();
        List<StudentApplicationDTOUsers> dtos = new ArrayList<>();
        when(repository.findStudentApplicationEntitiesByUsername(username)).thenReturn(entities);
        when(dtoUsersMapper.toDtoList(entities)).thenReturn(dtos);

        when(minioService.listFiles(anyString())).thenReturn(new ArrayList<>());

        List<StudentApplicationDTOUsers> result = service.findStudentApplicationsForUser(username);

        assertEquals(dtos, result);
        verify(repository).findStudentApplicationEntitiesByUsername(username);
        verify(dtoUsersMapper).toDtoList(entities);
    }

    @Test
    void testFindByStudentUsernameAndFacultyAndSpecialty() {
        String studentUsername = "testUser";
        String facultyName = "testFaculty";
        String specialtyName = "testSpecialty";
        List<StudentApplicationEntity> entities = new ArrayList<>();
        List<StudentApplicationDTO> dtos = new ArrayList<>();
        when(repository.findStudentApplicationEntitiesByUsername(studentUsername)).thenReturn(entities);
        when(repository.findStudentApplicationEntitiesByUsernameAndFacultyFacultyName(studentUsername, facultyName)).thenReturn(entities);
        when(mapper.toDtoList(entities)).thenReturn(dtos);

        List<StudentApplicationDTO> result = service.findByStudentUsernameAndFacultyAndSpecialty(studentUsername, facultyName, specialtyName);
        List<StudentApplicationDTO> result1 = service.findByStudentUsernameAndFacultyAndSpecialty(studentUsername, null, null);
        List<StudentApplicationDTO> result2 = service.findByStudentUsernameAndFacultyAndSpecialty(studentUsername, facultyName, null);

        assertEquals(dtos, result);
        verify(repository).findStudentApplicationEntitiesByUsername(studentUsername);
        verify(repository).findStudentApplicationEntitiesByUsernameAndFacultyFacultyName(studentUsername,facultyName);
        verify(mapper, times(2)).toDtoList(entities);
    }

    @Test
    void testFindByFacultyAndSpecialty() {
        String faculty = "testFaculty";
        String specialty = "testSpecialty";
        List<StudentApplicationEntity> entities = new ArrayList<>();
        List<StudentApplicationDTO> dtos = new ArrayList<>();
        when(repository.findStudentApplicationEntitiesByFacultyFacultyNameAndSpecialtySpecialtyName(faculty, specialty)).thenReturn(entities);
        when(mapper.toDtoList(entities)).thenReturn(dtos);

        List<StudentApplicationDTO> result = service.findByFacultyAndSpecialty(faculty, specialty);

        assertEquals(dtos, result);
        verify(repository).findStudentApplicationEntitiesByFacultyFacultyNameAndSpecialtySpecialtyName(faculty, specialty);
        verify(mapper).toDtoList(entities);
    }

    @Test
    void testFindByFaculty() {
        String faculty = "testFaculty";
        List<StudentApplicationEntity> entities = new ArrayList<>();
        List<StudentApplicationDTO> dtos = new ArrayList<>();
        when(repository.findStudentApplicationEntitiesByFacultyFacultyName(faculty)).thenReturn(entities);
        when(mapper.toDtoList(entities)).thenReturn(dtos);

        List<StudentApplicationDTO> result = service.findByFaculty(faculty);

        assertEquals(dtos, result);
        verify(repository).findStudentApplicationEntitiesByFacultyFacultyName(faculty);
        verify(mapper).toDtoList(entities);
    }

    @Test
    void testFindBySpecialty() {
        String specialty = "testSpecialty";
        List<StudentApplicationEntity> entities = new ArrayList<>();
        List<StudentApplicationDTO> dtos = new ArrayList<>();
        when(repository.findStudentApplicationEntitiesBySpecialtySpecialtyName(specialty)).thenReturn(entities);
        when(mapper.toDtoList(entities)).thenReturn(dtos);

        List<StudentApplicationDTO> result = service.findBySpecialty(specialty);

        assertEquals(dtos, result);
        verify(repository).findStudentApplicationEntitiesBySpecialtySpecialtyName(specialty);
        verify(mapper).toDtoList(entities);
    }

    @Test
    void testFindByStudentsApplicationsIdAndStatus() {
        String studentUsername = "testUser";
        String facultyName = "testFaculty";
        String specialtyName = "testSpecialty";
        StudentApplicationDTOUsers dtoUsers = new StudentApplicationDTOUsers();
        when(repository.findByStudentsApplicationsIdAndStatus(studentUsername, facultyName, specialtyName))
                .thenReturn(Optional.of(new StudentApplicationEntity()));
        when(dtoUsersMapper.toDto(any())).thenReturn(dtoUsers);

        StudentApplicationDTOUsers result = service.findByStudentsApplicationsIdAndStatus(studentUsername, facultyName, specialtyName);

        assertEquals(dtoUsers, result);
        verify(repository).findByStudentsApplicationsIdAndStatus(studentUsername, facultyName, specialtyName);
        verify(dtoUsersMapper).toDto(any());
    }

    @Test
    void testFindByStudentsApplicationsIdAndStatusAccepted() {
        String studentUsername = "testUser";
        String facultyName = "testFaculty";
        String specialtyName = "testSpecialty";
        StudentApplicationDTO dto = new StudentApplicationDTO();
        when(repository.findByStudentsApplicationsIdAndStatusAccepted(specialtyName, studentUsername))
                .thenReturn(Optional.of(new StudentApplicationEntity()));
        when(mapper.toDto(any())).thenReturn(dto);

        StudentApplicationDTO result = service.findByStudentsApplicationsIdAndStatusAccepted(studentUsername, facultyName, specialtyName);

        assertEquals(dto, result);
        verify(repository).findByStudentsApplicationsIdAndStatusAccepted(specialtyName, studentUsername);
        verify(mapper).toDto(any());
    }

    @Test
    void testFilterStudentApplications() throws Exception {
        FilterStudentApplicationsDTO filter = new FilterStudentApplicationsDTO();
        List<StudentApplicationEntity> entities = new ArrayList<>();
        List<StudentApplicationDTOUsers> dtos = new ArrayList<>();
        when(repositoryCustom.searchRecords(filter)).thenReturn(entities);
        when(dtoUsersMapper.toDtoList(entities)).thenReturn(dtos);

        when(minioService.listFiles(anyString())).thenReturn(new ArrayList<>());

        List<StudentApplicationDTOUsers> result = service.filterStudentApplications(filter);

        assertEquals(dtos, result);
        verify(repositoryCustom).searchRecords(filter);
        verify(dtoUsersMapper).toDtoList(entities);
    }

    @Test
    void testDeleteByUsernameAndAndFacultyAndSpecialty() throws Exception {
        String username = "testUser-";
        String facultyName = "testFaculty-1";
        String specialtyName = "testSpecialty-1";

        // Mock the repository method that is actually used in the service method
        when(repository.deleteByUsernameAndAndFacultyAndSpecialty(username, facultyName.split("-")[0], specialtyName.split("-")[0])).thenReturn(1);

        // Mock the repository method that is actually called
        when(repository.findByStudentsApplicationsId(username, facultyName, specialtyName))
                .thenReturn(Optional.of(new StudentApplicationEntity()));

        // Call the method under test
        int result = service.deleteByUsernameAndAndFacultyAndSpecialty(username, facultyName, specialtyName);

        // Verify results
        assertEquals(1, result);

        // Verify the repository method was called
        verify(repository).deleteByUsernameAndAndFacultyAndSpecialty(username, facultyName.split("-")[0], specialtyName.split("-")[0]);

        // Verify that the minioService method was called
        verify(minioService).deleteFolderByFullName(anyString());
    }


    @Test
    void testDeleteById() throws Exception {
        String username = "testUser";
        Long facultyId = 1L;
        Long specialtyId = 2L;

        service.deleteById(username, facultyId, specialtyId);

        verify(repository).deleteById(new StudentsApplicationsId(username, facultyId, specialtyId));
        verify(minioService).deleteFolderByFullName(anyString());
    }

    @Test
    void testDeleteByUsername() throws Exception {
        String username = "testUser";
        when(repository.deleteByUsername(username)).thenReturn(1);

        service.deleteByUsername(username);

        verify(repository).deleteByUsername(username);
        verify(minioService).deleteFolderByUsernamePrefix(username);
    }

    @Test
    void testFindDeclinedApplications() {
        List<StudentApplicationEntity> entities = new ArrayList<>();
        List<StudentApplicationDTO> dtos = new ArrayList<>();
        when(repository.findDeclinedApplications()).thenReturn(entities);
        when(mapper.toDtoList(entities)).thenReturn(dtos);

        List<StudentApplicationDTO> result = service.findDeclinedApplications();

        assertEquals(dtos, result);
        verify(repository).findDeclinedApplications();
        verify(mapper).toDtoList(entities);
    }

    @Test
    void testIsCachingEnabled() {
        assertFalse(service.isCachingEnabled());
    }

    @Test
    void testCreateApplication() throws Exception {
        // Create mock MultipartHttpServletRequest
        MultipartHttpServletRequest request = mock(MultipartHttpServletRequest.class);

        // Create mock Jwt
        Jwt principal = mock(Jwt.class);

        // Create mock StudentApplicationDTO
        StudentApplicationDTO dto = new StudentApplicationDTO();
        dto.setUsername("testUser");

        // Create a mock MultiValueMap and add files to it
        MultiValueMap<String, MultipartFile> multiValueMap = new LinkedMultiValueMap<>();
        MultipartFile file = mock(MultipartFile.class);
        multiValueMap.add("fileKey", file);

        // Create an iterator for file names
        Iterator<String> fileNamesIterator = Arrays.asList("fileKey").iterator();

        // Mock methods
        when(request.getParameter("studentApplicationCreateDTO")).thenReturn("{}");
        when(principal.getClaim("preferred_username")).thenReturn("testUser");
        when(request.getMultiFileMap()).thenReturn(multiValueMap);
        when(request.getFileNames()).thenReturn(fileNamesIterator);
        when(request.getFiles("fileKey")).thenReturn(Collections.singletonList(file));

        // Mock mapper behavior
        when(mapper.toDto(any())).thenReturn(dto);

        // Call the method under test
        StudentApplicationDTO result = service.createApplication(request, principal);

        // Assert results
        assertEquals(dto, result);
        verify(minioService).uploadFile(file, "testUser - Specialty - null - Faculty - null");
    }
}
