package bg.duosoft.uniapplicationdemo.validators;

import bg.duosoft.uniapplicationdemo.models.dtos.FacultyDTO;
import bg.duosoft.uniapplicationdemo.repositories.FacultyRepository;
import bg.duosoft.uniapplicationdemo.validators.config.ValidationError;
import bg.duosoft.uniapplicationdemo.validators.config.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FacultyValidator implements Validator<FacultyDTO> {
    @Override
    public List<ValidationError> validate(FacultyDTO facultyDTO, Object... args) {
        List<ValidationError> errors = new ArrayList<>();

        rejectIfEmpty(errors, facultyDTO, "facultyDTO", "m.validator.facultyDTO.empty");

        Boolean isCreate = null;

        if (args != null && args.length > 0 && args[0] instanceof Boolean) {
            isCreate = (Boolean) args[0];
        }

        if (errors.isEmpty()) {
            rejectIfEmptyString(errors, facultyDTO.getFacultyName(), "facultyName", "m.validator.facultyName.empty");
            rejectIfStringLengthBigger(errors,facultyDTO.getFacultyName(),255,"facultyName","m.validator.facultyName.length.tooLong");
            rejectIfEmptyDate(errors, facultyDTO.getEstablishedOn(), "establishedOn", "m.validator.establishedOn.empty");

            if (!hasValidationErrorPointer(errors, "totalNumberStudents")) {
                rejectIfNotPositiveNumber(errors, facultyDTO.getTotalNumberStudents(), "totalNumberStudents", "m.validator.totalNumberStudents.notPositive");
            }

            if (!hasValidationErrorPointer(errors, "description")) {
                rejectIfStringLengthBigger(errors, facultyDTO.getDescription(), MAX_INPUT_LENGTH_XL, "description", "m.validator.description.tooLong");
            }
        }

        if (isCreate != null && errors.isEmpty()) {
            FacultyRepository facultyRepository = (FacultyRepository) args[1];
            rejectIfTrue(errors, facultyRepository.findByFacultyName(facultyDTO.getFacultyName()).isEmpty(), "facultyName", "m.validator.facultyName.exists");
            if (isCreate) {
                rejectIfTrue(errors, facultyDTO.getId() != null, "id", "facultyDTO.id.not.empty");
            } else {
                rejectIfTrue(errors, facultyDTO.getId() == null, "id", "facultyDTO.id.not.empty");
            }
        }
        return errors;
    }

}
