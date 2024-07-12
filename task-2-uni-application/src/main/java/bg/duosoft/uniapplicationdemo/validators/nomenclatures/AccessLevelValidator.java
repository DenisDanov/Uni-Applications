package bg.duosoft.uniapplicationdemo.validators.nomenclatures;

import bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures.AccessLevelDTO;
import bg.duosoft.uniapplicationdemo.repositories.AccessLevelRepository;
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
public class AccessLevelValidator implements Validator<AccessLevelDTO> {

    private final AccessLevelRepository accessLevelRepository;

    @Override
    public List<ValidationError> validate(AccessLevelDTO accessLevelDTO, Object... args) {
        List<ValidationError> errors = new ArrayList<>();
        Boolean isCreate = null;

        if (args != null && args.length > 0 && args[0] instanceof Boolean) {
            isCreate = (Boolean) args[0];
        }

        rejectIfEmpty(errors, accessLevelDTO, "accessLevelDTO", "m.validator.accessLevelDTO.empty");

        if (errors.isEmpty()) {
            rejectIfEmptyString(errors, accessLevelDTO.getAccessType(), "accessType", "m.validator.accessType.empty");
            rejectIfStringLengthBigger(errors,accessLevelDTO.getAccessType(),50,"accessType", "m.validator.accessLevelDTO.accessType.length.tooLong");
            rejectIfTrue(errors,!accessLevelDTO.getAccessType().equals("READ_ONLY_ACCESS") && !accessLevelDTO.getAccessType().equals("FULL_ACCESS"),
                    "accessType", "m.validator.accessLevelDTO.accessType.invalid");
        }

        if (isCreate != null) {
            rejectIfTrue(
                    errors,
                    accessLevelRepository.findByAccessType(accessLevelDTO.getAccessType()).isPresent(),
                    "accessType",
                    "m.validator.accessType.exists");
        } else {
            rejectIfFalse(
                    errors,
                    accessLevelRepository.findByAccessType(accessLevelDTO.getAccessType()).isPresent(),
                    "accessType",
                    "m.validator.accessType.dont.exists");
        }

        return errors;
    }
}
