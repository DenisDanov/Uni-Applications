package bg.duosoft.uniapplicationdemo.services;

import bg.duosoft.uniapplicationdemo.models.dtos.FilterUsersDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.UserDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures.RoleDTO;
import org.keycloak.representations.idm.UserRepresentation;

import jakarta.ws.rs.core.Response;
import java.sql.Date;
import java.util.List;

public interface KeycloakUserService {

    Response createUser(String username, String email, String firstName, String middleName, String lastName, String password, Date dateOfBirth, String phoneNumber, RoleDTO roleDTO);

    List<UserRepresentation> getAllUsers();

    UserRepresentation getUserByUsername(String username);

    UserDTO update(UserDTO userDTO);

    void delete(UserDTO userDTO);

    void deleteById(String id);

    UserRepresentation getUserByEmail(String email);

    boolean manageBlockingUser(String username, String blockStatus);

    UserDTO updateUserRoles(UserDTO userDTO);

    List<UserRepresentation> filterUsers(FilterUsersDTO filterUsersDTO);
}
