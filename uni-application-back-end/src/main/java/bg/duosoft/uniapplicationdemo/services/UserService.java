package bg.duosoft.uniapplicationdemo.services;

import bg.duosoft.uniapplicationdemo.models.dtos.FilterUsersDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.UserDTO;
import bg.duosoft.uniapplicationdemo.services.base.BaseService;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UserService extends BaseService<String, UserDTO> {
    ResponseEntity<Object> registerUser(UserDTO userDTO);

    Optional<UserDTO> findByUsername(String username);

    Optional<UserDTO> findByEmail(String email);

    boolean manageBlockStatus(String username, String blockStatus);

    UserDTO updateUserRoles(UserDTO userDTO) throws Exception;

    List<UserDTO> filterUsers(FilterUsersDTO filterUsersDTO);
}
