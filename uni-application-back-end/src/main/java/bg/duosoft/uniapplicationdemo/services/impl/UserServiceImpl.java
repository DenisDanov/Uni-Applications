package bg.duosoft.uniapplicationdemo.services.impl;

import bg.duosoft.uniapplicationdemo.events.UserRegisteredEvent;
import bg.duosoft.uniapplicationdemo.mappers.UserMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.FilterUsersDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.UserDTO;
import bg.duosoft.uniapplicationdemo.services.KeycloakUserService;
import bg.duosoft.uniapplicationdemo.services.StudentApplicationService;
import bg.duosoft.uniapplicationdemo.services.UserService;
import bg.duosoft.uniapplicationdemo.validators.UserValidator;
import bg.duosoft.uniapplicationdemo.validators.config.ValidationError;
import bg.duosoft.uniapplicationdemo.validators.config.ValidationErrorException;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Lazy
    @Autowired
    private StudentApplicationService studentApplicationService;

    private final UserValidator userValidator;

    private final KeycloakUserService keycloakUserService;

    private final UserMapper userMapper;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public ResponseEntity<Object> registerUser(UserDTO userDTO) {
        List<ValidationError> errors = userValidator.validate(userDTO, true, this);
        if (errors.isEmpty()) {
            try {
                create(userDTO);
                eventPublisher.publishEvent(new UserRegisteredEvent(this, userDTO));
                return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
            } catch (ResponseStatusException e) {
                return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
    }

    @Override
    public List<UserDTO> getAll() {
        return keycloakUserService.getAllUsers().stream().map(userMapper::toDto).toList();
    }

    @Override
    public Optional<UserDTO> findByUsername(String username) {
        UserRepresentation user = keycloakUserService.getUserByUsername(username);
        if (user == null) {
            return Optional.empty();
        } else {
            return Optional.of(userMapper.toDto(user));
        }
    }

    @Override
    public Optional<UserDTO> findByEmail(String email) {
        UserRepresentation user = keycloakUserService.getUserByEmail(email);
        if (user == null) {
            return Optional.empty();
        } else {
            return Optional.of(userMapper.toDto(user));
        }
    }

    @Override
    public boolean manageBlockStatus(String username, String blockStatus) {
        return keycloakUserService.manageBlockingUser(username, blockStatus);
    }

    @Override
    public UserDTO updateUserRoles(UserDTO userDTO) throws Exception {
        if (userDTO.getRoleDTO().getRole().equals("ADMIN")) {
            studentApplicationService.deleteByUsername(userDTO.getUsername());
        }
        return keycloakUserService.updateUserRoles(userDTO);
    }

    @Override
    public List<UserDTO> filterUsers(FilterUsersDTO filterUsersDTO) {
        return userMapper.toDtoList(keycloakUserService.filterUsers(filterUsersDTO));
    }

    @Override
    public UserDTO create(UserDTO userDTO) {
        try (Response response = keycloakUserService.createUser(
                userDTO.getUsername(),
                userDTO.getEmail(),
                userDTO.getFirstName(),
                userDTO.getMiddleName(),
                userDTO.getLastName(),
                userDTO.getPassword(),
                userDTO.getDateOfBirth(),
                userDTO.getPhoneNumber(),
                userDTO.getRoleDTO()

        )) {
            if (response.getStatus() == 201) {
                return getById(userDTO.getUsername());
            } else {
                throw new ResponseStatusException(HttpStatusCode.valueOf(response.getStatus()), response.getStatusInfo().getReasonPhrase());
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public UserDTO getById(String username) {
        return userMapper.toDto(keycloakUserService.getUserByUsername(username));
    }

    @Override
    public UserDTO update(UserDTO userDTO) {
        List<ValidationError> errors = userValidator.validate(userDTO);
        if (errors.isEmpty()) {
            return keycloakUserService.update(userDTO);
        } else {
            throw new ValidationErrorException(errors);
        }
    }

    @Override
    public void delete(UserDTO userDTO) throws Exception {
        keycloakUserService.delete(userDTO);
        studentApplicationService.deleteByUsername(userDTO.getUsername());
    }

    @Override
    public void deleteById(String id) throws Exception {
        keycloakUserService.deleteById(id);
        studentApplicationService.deleteByUsername(id);
    }

    @Override
    public String getCacheName() {
        return this.getClass().getSimpleName();
    }

}
