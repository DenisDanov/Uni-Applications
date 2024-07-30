package bg.duosoft.uniapplicationdemo.services;

import bg.duosoft.uniapplicationdemo.events.UserRegisteredEvent;
import bg.duosoft.uniapplicationdemo.mappers.UserMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.FilterUsersDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.UserDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures.RoleDTO;
import bg.duosoft.uniapplicationdemo.services.impl.UserServiceImpl;
import bg.duosoft.uniapplicationdemo.validators.UserValidator;
import bg.duosoft.uniapplicationdemo.validators.config.ValidationError;
import bg.duosoft.uniapplicationdemo.validators.config.ValidationErrorException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private StudentApplicationService studentApplicationService;

    @Mock
    private KeycloakUserService keycloakUserService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserValidator userValidator;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser_Success() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setEmail("test@example.com");
        userDTO.setFirstName("Test");
        userDTO.setMiddleName("Middle");
        userDTO.setLastName("User");
        userDTO.setPassword("password");
        userDTO.setDateOfBirth(Date.valueOf("2000-01-01"));
        userDTO.setPhoneNumber("123456789");
        userDTO.setRoleDTO(new RoleDTO("USER", "User role"));

        // Mock the Response object
        Response mockResponse = mock(Response.class);
        when(mockResponse.getStatus()).thenReturn(201);
        when(mockResponse.getStatusInfo()).thenReturn(Response.Status.CREATED);

        // Mock the dependencies
        when(userValidator.validate(any(UserDTO.class), anyBoolean(), any())).thenReturn(new ArrayList<>());
        when(keycloakUserService.createUser(
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                any(), any(), any())).thenReturn(mockResponse);
        when(keycloakUserService.getUserByUsername(anyString())).thenReturn(null); // Mock getUserByUsername if needed

        // Call the method to test
        ResponseEntity<Object> response = userServiceImpl.registerUser(userDTO);

        // Assertions
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User created successfully", response.getBody());

        // Verify interactions
        verify(eventPublisher).publishEvent(any(UserRegisteredEvent.class));
    }

    @Test
    public void testRegisterUser_ValidationError() {
        UserDTO userDTO = new UserDTO();
        List<ValidationError> errors = List.of(ValidationError.create("errorCode", "errorMessage"));

        when(userValidator.validate(any(UserDTO.class), anyBoolean(), any())).thenReturn(errors);

        ResponseEntity<Object> response = userServiceImpl.registerUser(userDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errors, response.getBody());
    }

    @Test
    public void testFindByUsername_Found() {
        String username = "test";
        UserRepresentation userRepresentation = new UserRepresentation();
        UserDTO userDTO = new UserDTO();

        // Mocking the behavior of dependencies
        when(keycloakUserService.getUserByUsername(username)).thenReturn(userRepresentation);
        when(userMapper.toDto(userRepresentation)).thenReturn(userDTO);

        // Call the method to test
        Optional<UserDTO> result = userServiceImpl.findByUsername(username);

        // Assertions
        assertTrue(result.isPresent(), "Expected Optional to be present but it was empty");
        assertEquals(userDTO, result.get(), "Expected UserDTO to match the mocked value");
    }

    @Test
    public void testFindByUsername_NotFound() {
        String username = "testUser";

        when(keycloakUserService.getUserByUsername(username)).thenReturn(null);

        Optional<UserDTO> result = userServiceImpl.findByUsername(username);

        assertFalse(result.isPresent());
    }

    @Test
    public void testUpdateUserRoles_AdminRole() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setRoleDTO(new RoleDTO("ADMIN", "Administrator role"));
        userDTO.setUsername("testUser");

        when(keycloakUserService.updateUserRoles(userDTO)).thenReturn(userDTO);
        doNothing().when(studentApplicationService).deleteByUsername("testUser");

        UserDTO updatedUserDTO = userServiceImpl.updateUserRoles(userDTO);

        assertEquals(userDTO, updatedUserDTO);
        verify(studentApplicationService).deleteByUsername("testUser");
    }

    @Test
    public void testUpdateUserRoles_OtherRole() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setRoleDTO(new RoleDTO("USER", "Administrator role"));

        when(keycloakUserService.updateUserRoles(userDTO)).thenReturn(userDTO);

        UserDTO updatedUserDTO = userServiceImpl.updateUserRoles(userDTO);

        assertEquals(userDTO, updatedUserDTO);
        verify(studentApplicationService, never()).deleteByUsername(anyString());
    }

    @Test
    public void testUpdate_InvalidUser() {
        UserDTO userDTO = new UserDTO();
        List<ValidationError> errors = List.of(ValidationError.create("errorCode", "errorMessage"));

        when(userValidator.validate(userDTO)).thenReturn(errors);

        assertThrows(ValidationErrorException.class, () -> userServiceImpl.update(userDTO));
    }

    @Test
    public void testDeleteUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");

        doNothing().when(keycloakUserService).delete(userDTO);
        doNothing().when(studentApplicationService).deleteByUsername("testUser");

        userServiceImpl.delete(userDTO);

        verify(keycloakUserService).delete(userDTO);
        verify(studentApplicationService).deleteByUsername("testUser");
    }

    @Test
    public void testFilterUsers() {
        FilterUsersDTO filterUsersDTO = new FilterUsersDTO();
        List<UserRepresentation> userRepresentations = List.of(new UserRepresentation());
        List<UserDTO> userDTOs = List.of(new UserDTO());

        when(keycloakUserService.filterUsers(filterUsersDTO)).thenReturn(userRepresentations);
        when(userMapper.toDtoList(userRepresentations)).thenReturn(userDTOs);

        List<UserDTO> result = userServiceImpl.filterUsers(filterUsersDTO);

        assertEquals(userDTOs, result);
    }
}
