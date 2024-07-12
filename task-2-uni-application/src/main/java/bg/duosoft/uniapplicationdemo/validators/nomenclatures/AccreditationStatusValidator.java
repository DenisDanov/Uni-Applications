package bg.duosoft.uniapplicationdemo.validators.nomenclatures;

import bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures.AccreditationStatusDTO;
import bg.duosoft.uniapplicationdemo.repositories.AccreditationStatusRepository;
import bg.duosoft.uniapplicationdemo.validators.config.ValidationError;
import bg.duosoft.uniapplicationdemo.validators.config.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AccreditationStatusValidator implements Validator<AccreditationStatusDTO> {

    private final AccreditationStatusRepository accreditationStatusRepository;

    @Override
    public List<ValidationError> validate(AccreditationStatusDTO accreditationStatusDTO, Object... args) {
        List<ValidationError> errors = new ArrayList<>();
        Boolean isCreate = null;

        if (args != null && args.length > 0 && args[0] instanceof Boolean) {
            isCreate = (Boolean) args[0];
        }

        rejectIfEmpty(errors, accreditationStatusDTO, "accreditationStatusDTO", "m.validator.accreditationStatusDTO.empty");

        if (errors.isEmpty()) {
            rejectIfEmptyString(errors, accreditationStatusDTO.getAccreditationType(), "accreditationType", "m.validator.accreditationType.empty");
            rejectIfStringLengthBigger(errors, accreditationStatusDTO.getAccreditationType(), 30,"accreditationStatus", "m.validator.accreditationStatusDTO.accreditationStatus.length.tooLong");
        }

        if (isCreate != null) {
            rejectIfTrue(
                    errors,
                    accreditationStatusRepository.findByAccreditationType(accreditationStatusDTO.getAccreditationType()).isPresent(),
                    "accreditationType",
                    "m.validator.accreditationType.exists");
        } else {
            rejectIfFalse(errors,
                    accreditationStatusRepository.findByAccreditationType(accreditationStatusDTO.getAccreditationType()).isPresent(),
                    "accreditationType",
                    "m.validator.accreditationType.dont.exists");
        }

        return errors;
    }

}
