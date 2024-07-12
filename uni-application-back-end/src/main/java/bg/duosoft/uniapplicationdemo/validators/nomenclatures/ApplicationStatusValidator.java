package bg.duosoft.uniapplicationdemo.validators.nomenclatures;

import bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures.ApplicationStatusDTO;
import bg.duosoft.uniapplicationdemo.repositories.ApplicationStatusRepository;
import bg.duosoft.uniapplicationdemo.validators.config.ValidationError;
import bg.duosoft.uniapplicationdemo.validators.config.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ApplicationStatusValidator implements Validator<ApplicationStatusDTO> {

    private final ApplicationStatusRepository applicationStatusRepository;

    @Override
    public List<ValidationError> validate(ApplicationStatusDTO applicationStatusDTO, Object... args) {
        List<ValidationError> errors = new ArrayList<>();
        Boolean isCreate = null;

        if (args != null && args.length > 0 && args[0] instanceof Boolean) {
            isCreate = (Boolean) args[0];
        }

        rejectIfEmpty(errors, applicationStatusDTO, "applicationStatusDTO", "m.validator.applicationStatusDTO.empty");

        if (errors.isEmpty()) {
            rejectIfEmptyString(errors, applicationStatusDTO.getApplicationStatus(), "applicationStatusEnum", "m.validator.applicationStatusEnum.empty");
            rejectIfStringLengthBigger(errors,applicationStatusDTO.getApplicationStatus(),30,"applicationStatus", "m.validator.applicationStatusDTO.applicationStatus.length.tooLong");
        }

        if (isCreate != null) {
            rejectIfTrue(
                    errors,
                    applicationStatusRepository.findByApplicationStatus(applicationStatusDTO.getApplicationStatus()).isPresent(),
                    "applicationStatusEnum",
                    "m.validator.applicationStatus.exists");
        } else {
            rejectIfFalse(
                    errors,
                    applicationStatusRepository.findByApplicationStatus(applicationStatusDTO.getApplicationStatus()).isPresent(),
                    "applicationStatusEnum",
                    "m.validator.applicationStatus.dont.exists");
        }

        return errors;
    }
}
