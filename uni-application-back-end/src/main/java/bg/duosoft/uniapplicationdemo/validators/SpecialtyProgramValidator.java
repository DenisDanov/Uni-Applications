package bg.duosoft.uniapplicationdemo.validators;

import bg.duosoft.uniapplicationdemo.models.dtos.SpecialtyProgramDTO;
import bg.duosoft.uniapplicationdemo.validators.config.ValidationError;
import bg.duosoft.uniapplicationdemo.validators.config.Validator;
import bg.duosoft.uniapplicationdemo.validators.nomenclatures.AccreditationStatusValidator;
import bg.duosoft.uniapplicationdemo.validators.nomenclatures.DegreeTypeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SpecialtyProgramValidator implements Validator<SpecialtyProgramDTO> {

    private final DegreeTypeValidator degreeTypeValidator;

    private final AccreditationStatusValidator accreditationStatusValidator;

    @Override
    public List<ValidationError> validate(SpecialtyProgramDTO specialtyProgramDTO, Object... args) {
        List<ValidationError> errors = new ArrayList<>();
        Boolean isCreate = null;

        if (args != null && args.length > 0 && args[0] instanceof Boolean) {
            isCreate = (Boolean) args[0];
        }

        rejectIfEmpty(errors, specialtyProgramDTO, "specialtyProgramDTO", "m.validator.specialtyProgramDTO.empty");

        if (errors.isEmpty()) {
            rejectIfEmptyDate(errors, specialtyProgramDTO.getStartsOn(), "startsOn", "m.validator.startsOn.empty");
            rejectIfEmptyDate(errors, specialtyProgramDTO.getEndsOn(), "endsOn", "m.validator.endsOn.empty");
            rejectIfNumberIsNull(errors, specialtyProgramDTO.getDurationDays(), "durationDays", "m.validator.durationDays.empty");
            rejectIfEmptyString(errors, specialtyProgramDTO.getProgramName(), "programName", "m.validator.programName.empty");
            errors.addAll(degreeTypeValidator.validate(specialtyProgramDTO.getDegreeType()));
            rejectIfEmptyString(errors, specialtyProgramDTO.getDescription(), "description", "m.validator.description.empty");
            errors.addAll(accreditationStatusValidator.validate(specialtyProgramDTO.getAccreditationStatus()));

            if (!hasValidationErrorPointer(errors, "startsOn") && !hasValidationErrorPointer(errors, "endsOn")) {
                rejectIfFirstDateIsBeforeSecond(errors, specialtyProgramDTO.getEndsOn(), specialtyProgramDTO.getStartsOn(), "endsOn", "m.validator.endsOn.before.startsOn");
            }

            if (!hasValidationErrorPointer(errors, "durationDays")) {
                rejectIfNotPositiveNumber(errors, specialtyProgramDTO.getDurationDays(), "durationDays", "m.validator.durationDays.notPositive");
            }

            if (!hasValidationErrorPointer(errors, "description")) {
                rejectIfStringLengthBigger(errors, specialtyProgramDTO.getDescription(), MAX_INPUT_LENGTH_XL, "description", "m.validator.description.tooLong");
            }

            rejectIfStringLengthBigger(errors, specialtyProgramDTO.getProgramName(), 255, "programName", "m.validator.programName.tooLong");
        }

        if (errors.isEmpty() && isCreate != null && isCreate) {
            rejectIfTrue(errors, specialtyProgramDTO.getId() != null, "id", "m.validator.id.not.empty");
        } else if (errors.isEmpty() && isCreate != null) {
            rejectIfTrue(errors, specialtyProgramDTO.getId() == null, "id", "m.validator.id.empty");
        }

        return errors;
    }

}
