package bg.duosoft.uniapplicationdemo.validators;

import bg.duosoft.uniapplicationdemo.models.dtos.UserDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures.AccessLevelDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures.RoleDTO;
import bg.duosoft.uniapplicationdemo.services.UserService;
import bg.duosoft.uniapplicationdemo.validators.config.ValidationError;
import bg.duosoft.uniapplicationdemo.validators.config.Validator;
import bg.duosoft.uniapplicationdemo.validators.nomenclatures.AccessLevelValidator;
import bg.duosoft.uniapplicationdemo.validators.nomenclatures.RoleValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserValidator implements Validator<UserDTO> {

    private final RoleValidator roleValidator;

    private final AccessLevelValidator accessLevelValidator;

    @Override
    public List<ValidationError> validate(UserDTO userDTO, Object... args) {
        List<ValidationError> errors = new ArrayList<>();
        Boolean isCreate = null;

        if (args != null && args.length > 0 && args[0] instanceof Boolean) {
            isCreate = (Boolean) args[0];
        }

        rejectIfEmpty(errors, userDTO, "user", "User information cannot be empty.");

        if (errors.isEmpty()) {
            rejectIfEmptyString(errors, userDTO.getUsername(), "username", "Username cannot be empty.");
            rejectIfEmptyString(errors, userDTO.getEmail(), "email", "Email cannot be empty.");
            rejectIfNotMatchRegex(errors, userDTO.getEmail(), "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", "email", "Invalid email");
            rejectIfEmptyString(errors, userDTO.getFirstName(), "firstName", "First name cannot be empty.");
            rejectIfEmptyString(errors, userDTO.getMiddleName(), "middleName", "Middle name cannot be empty.");
            rejectIfEmptyString(errors, userDTO.getLastName(), "lastName", "Last name cannot be empty.");
            rejectIfNotMatchRegex(errors, userDTO.getFirstName(), "^[^0-9]*$", "firstName", "First name cannot contain numbers.");
            rejectIfNotMatchRegex(errors, userDTO.getMiddleName(), "^[^0-9]*$", "middleName", "Middle name cannot contain numbers.");
            rejectIfNotMatchRegex(errors, userDTO.getLastName(), "^[^0-9]*$", "lastName", "Last name cannot contain numbers.");
            rejectIfStringLengthBigger(errors, userDTO.getUsername(), 255, "username", "Username cannot exceed 255 characters.");
            rejectIfStringLengthBigger(errors, userDTO.getEmail(), 255, "email", "Email cannot exceed 255 characters.");
            rejectIfStringLengthBigger(errors, userDTO.getFirstName(), 255, "firstName", "First name cannot exceed 255 characters.");
            rejectIfStringLengthBigger(errors, userDTO.getMiddleName(), 255, "middleName", "Middle name cannot exceed 255 characters.");
            rejectIfStringLengthBigger(errors, userDTO.getLastName(), 255, "lastName", "Last name cannot exceed 255 characters.");

            RoleDTO roleDTO = userDTO.getRoleDTO();
            AccessLevelDTO accessLevelDTO = userDTO.getAccessLevel();

            errors.addAll(this.roleValidator.validate(roleDTO));

            if (!hasValidationErrorPointer(errors, "roleDTO")) {
                if (roleDTO.getRole().equals("ADMIN")) {
                    errors.addAll(this.accessLevelValidator.validate(accessLevelDTO));
                } else if (roleDTO.getRole().equals("STUDENT")) {
                    rejectIfEmptyDate(errors, userDTO.getDateOfBirth(), "dateOfBirth", "Date of birth cannot be empty.");
                    rejectIfEmptyString(errors, userDTO.getPhoneNumber(), "phoneNumber", "Phone number cannot be empty.");
                    rejectIfNotMatchRegex(errors, userDTO.getPhoneNumber(), "^(\\+?[1-9]\\d{1,14}$|^\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}$)", "phoneNumber",
                            "Invalid phone number please try again.");
                }
            }
        }

        if (isCreate != null && isCreate && errors.isEmpty()) {
            UserService userService = (UserService) args[1];
            Optional<UserDTO> userServiceByUsername = userService.findByUsername(userDTO.getUsername());
            userServiceByUsername.ifPresent(dto -> rejectIfTrue(errors, dto.getUsername().equals(userDTO.getUsername()), "username", "Username already exists."));
            rejectIfTrue(errors, userService.findByEmail(userDTO.getEmail()).isPresent(), "email", "Email already exists.");
        }

        return errors;
    }
}
