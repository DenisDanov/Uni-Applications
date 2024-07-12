package bg.duosoft.uniapplicationdemo.validators.nomenclatures;

import bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures.RoleDTO;
import bg.duosoft.uniapplicationdemo.repositories.RoleRepository;
import bg.duosoft.uniapplicationdemo.validators.config.ValidationError;
import bg.duosoft.uniapplicationdemo.validators.config.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoleValidator implements Validator<RoleDTO> {

    private final RoleRepository roleRepository;

    @Override
    public List<ValidationError> validate(RoleDTO role, Object... args) {
        List<ValidationError> errors = new ArrayList<>();
        Boolean isCreate = null;

        if (args != null && args.length > 0 && args[0] instanceof Boolean) {
            isCreate = (Boolean) args[0];
        }

        rejectIfEmpty(errors, role, "roleDTO", "m.validator.roleDTO.empty");
        rejectIfEmptyString(errors, role.getRole(), "roleDTO", "m.validator.roleDTO.empty");

        if (errors.isEmpty()) {
            rejectIfTrue(errors,!role.getRole().equals("ADMIN") && !role.getRole().equals("STUDENT"), "m.validator.role", "m.validator.role.dontMatch");
        }
        if (errors.isEmpty() && isCreate != null) {
            rejectIfEmptyString(errors, role.getRole(), "role", "m.validator.roleDTO.roll.empty");
            rejectIfStringLengthBigger(errors,role.getRole(),30,"role", "m.validator.roleDTO.roll.length.tooLong");

            if (isCreate) {
                rejectIfTrue(errors, roleRepository.findById(role.getRole()).isPresent(), "m.validator.roleDTO.role", "m.validator.roleDTO.role.dontMatchValues");
            } else {
                rejectIfFalse(errors, roleRepository.findById(role.getRole()).isPresent(), "m.validator.roleDTO.role", "m.validator.roleDTO.role.dontMatchValues");
            }

        }
        return errors;
    }
}
