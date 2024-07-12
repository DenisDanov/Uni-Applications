package bg.duosoft.uniapplicationdemo.validators;

import bg.duosoft.uniapplicationdemo.models.dtos.FacultyDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.SpecialtyDTO;
import bg.duosoft.uniapplicationdemo.repositories.SpecialtyRepository;
import bg.duosoft.uniapplicationdemo.validators.config.ValidationError;
import bg.duosoft.uniapplicationdemo.validators.config.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SpecialtyValidator implements Validator<SpecialtyDTO> {

    private final SpecialtyProgramValidator specialtyProgramValidator;

    private final SpecialtyRequirementValidator specialtyRequirementValidator;

    private final FacultyValidator facultyValidator;

    private final SubjectValidator subjectValidator;

    @Override
    public List<ValidationError> validate(SpecialtyDTO specialtyDTO, Object... args) {
        List<ValidationError> errors = new ArrayList<>();
        Boolean isCreate = null;

        if (args != null && args.length > 0 && args[0] instanceof Boolean) {
            isCreate = (Boolean) args[0];
        }

        rejectIfEmpty(errors, specialtyDTO, "specialtyDTO", "m.validator.specialtyDTO.empty");

        if (errors.isEmpty()) {
            rejectIfEmptyString(errors, specialtyDTO.getSpecialtyName(), "specialtyName", "m.validator.specialtyName.empty");
            rejectIfStringLengthBigger(errors,specialtyDTO.getSpecialtyName(),50,"specialtyName","m.validator.specialtyName.length.tooLong");
            errors.addAll(specialtyRequirementValidator.validate(specialtyDTO.getSpecialtyRequirement()));
            errors.addAll(specialtyProgramValidator.validate(specialtyDTO.getSpecialtyProgram()));

            rejectIfEmptyCollection(errors, specialtyDTO.getSubjects(), "subjects", "m.validator.subjects.empty");

            if (!hasValidationErrorPointer(errors,"subjects")) {
                specialtyDTO.getSubjects().forEach(subjectDTO -> errors.addAll(subjectValidator.validate(subjectDTO)));
            }

            if (specialtyDTO.getEmploymentRate() != null) {
                if (specialtyDTO.getEmploymentRate() < 0.0 || specialtyDTO.getEmploymentRate() > 100.0) {
                    reject(errors, "employmentRate", "m.validator.employmentRate.invalidRange");
                }
            }
        }

        if (isCreate != null && errors.isEmpty()) {
            SpecialtyRepository specialtyRepository = (SpecialtyRepository) args[1];
            FacultyDTO facultyDTO = (FacultyDTO) args[2];
            rejectIfTrue(errors, specialtyRepository.findBySpecialtyName(specialtyDTO.getSpecialtyName()).isPresent(), "specialtyName", "m.validator.specialtyName.exists");
            errors.addAll(facultyValidator.validate(facultyDTO));
            if (isCreate) {
                rejectIfTrue(errors, Objects.nonNull(specialtyDTO.getId()), "id", "m.validation.id.not.empty");
            } else {
                rejectIfTrue(errors, !Objects.nonNull(specialtyDTO.getId()), "id", "m.validation.id.empty");
            }
        }

        return errors;
    }
}
