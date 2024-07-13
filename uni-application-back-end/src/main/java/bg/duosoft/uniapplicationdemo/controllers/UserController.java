package bg.duosoft.uniapplicationdemo.controllers;

import bg.duosoft.uniapplicationdemo.controllers.base.CrudController;
import bg.duosoft.uniapplicationdemo.models.dtos.FileMetadataDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.FilterUsersDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.UserDTO;
import bg.duosoft.uniapplicationdemo.security.SecurityUtils;
import bg.duosoft.uniapplicationdemo.services.UserService;
import bg.duosoft.uniapplicationdemo.services.impl.MinioService;
import bg.duosoft.uniapplicationdemo.validators.config.ValidationErrorException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController extends CrudController<String, UserDTO, UserService> {

    private final MinioService minioService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/get")
    public UserDTO getUserDataByUsername() {
        Jwt principal = SecurityUtils.getJwt();
        return super.getService().findByUsername(principal.getClaim("preferred_username")).orElse(null);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody UserDTO userDTO) {
        return super.getService().registerUser(userDTO);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ROLE_admin') and hasAnyRole('ROLE_READ_ONLY_ACCESS','ROLE_FULL_ACCESS')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(super.getService().getAll());
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ROLE_admin') and hasAnyRole('ROLE_READ_ONLY_ACCESS','ROLE_FULL_ACCESS')")
    public ResponseEntity<Optional<UserDTO>> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(super.getService().findByUsername(username));
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ROLE_admin') or hasRole('ROLE_student')")
    public ResponseEntity<Object> updateUser(@RequestBody UserDTO userDTO) {
        Jwt principal = SecurityUtils.getJwt();
        if (principal.getClaim("preferred_username").equals(userDTO.getUsername())) {
            try {
                UserDTO update = super.getService().update(userDTO);
                return ResponseEntity.ok(update);
            } catch (ValidationErrorException e) {
                return ResponseEntity.badRequest().body(e.getErrors());
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/update/roles")
    @PreAuthorize("hasRole('ROLE_admin') and hasRole('ROLE_FULL_ACCESS')")
    public ResponseEntity<Object> updateUserRoles(@RequestBody UserDTO userDTO) {
        try {
            UserDTO update = super.getService().updateUserRoles(userDTO);
            return ResponseEntity.ok(update);
        } catch (ValidationErrorException e) {
            return ResponseEntity.badRequest().body(e.getErrors());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body("Could not delete user application files.");
        }
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_admin') and hasRole('ROLE_FULL_ACCESS')")
    public ResponseEntity<Object> deleteUser(@RequestBody UserDTO userDTO) {
        try {
            super.getService().delete(userDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{username}")
    @PreAuthorize("hasRole('ROLE_admin') and hasRole('ROLE_FULL_ACCESS')")
    public ResponseEntity<Object> deleteUserByUsername(@PathVariable String username) {
        try {
            super.getService().deleteById(username);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{username}/{block}")
    @PreAuthorize("hasRole('ROLE_admin') and hasRole('ROLE_FULL_ACCESS')")
    public ResponseEntity<Object> blockUser(@PathVariable String username, @PathVariable String block) {
        if (block.equals("block") || block.equals("unblock")) {
            if (super.getService().manageBlockStatus(username, block)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().body("User not found or is blocked/unblocked already.");
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/user-files")
    @PreAuthorize("hasRole('ROLE_student')")
    public ResponseEntity<List<FileMetadataDTO>> listUserFiles() {
        Jwt principal = SecurityUtils.getJwt();
        try {
            String username = principal.getClaim("preferred_username");
            List<FileMetadataDTO> userFiles = minioService.listFiles(username);
            return ResponseEntity.ok(userFiles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }

    @PostMapping("/users/filter")
    @PreAuthorize("hasRole('ROLE_admin') and hasAnyRole('ROLE_READ_ONLY_ACCESS','ROLE_FULL_ACCESS')")
    public ResponseEntity<List<UserDTO>> filterUsers(@RequestBody FilterUsersDTO filterUsersDTO) {
        if (filterUsersDTO == null) {
            return ResponseEntity.ok(super.getAll());
        } else if (!StringUtils.hasText(filterUsersDTO.getPhoneNumber()) && !StringUtils.hasText(filterUsersDTO.getEmail()) && !StringUtils.hasText(filterUsersDTO.getUsername())){
            List<UserDTO> allUsers = super.getAll();
            return ResponseEntity.ok(filterUsersDTO.getMaxResults() < 1 ? allUsers : filterUsersDTO.getMaxResults() < allUsers.size() ? allUsers.subList(0, filterUsersDTO.getMaxResults()) : allUsers);
        } else {
            return ResponseEntity.ok(super.getService().filterUsers(filterUsersDTO));
        }
    }
}
