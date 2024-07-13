package bg.duosoft.uniapplicationdemo.validators.nomenclatures;

import bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures.DegreeTypeDTO;
import bg.duosoft.uniapplicationdemo.repositories.DegreeTypeRepository;
import bg.duosoft.uniapplicationdemo.validators.config.ValidationError;
import bg.duosoft.uniapplicationdemo.validators.config.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DegreeTypeValidator implements Validator<DegreeTypeDTO> {

    private final DegreeTypeRepository degreeTypeRepository;

    @Override
    public List<ValidationError> validate(DegreeTypeDTO degreeTypeDTO, Object... args) {
        List<ValidationError> errors = new ArrayList<>();
        Boolean isCreate = null;

        if (args != null && args.length > 0 && args[0] instanceof Boolean) {
            isCreate = (Boolean) args[0];
        }
        rejectIfEmpty(errors, degreeTypeDTO, "degreeTypeDTO", "m.validator.degreeTypeDTO.empty");

        if (errors.isEmpty() && isCreate != null) {
            rejectIfEmptyString(errors, degreeTypeDTO.getDegreeType(), "degreeType", "m.validator.degreeType.empty");
            rejectIfStringLengthBigger(errors,degreeTypeDTO.getDegreeType(),30,"degreeType", "m.validator.degreeTypeDTO.degreeType.length.tooLong");
            if (isCreate) {
                rejectIfTrue(errors, degreeTypeRepository.findById(degreeTypeDTO.getDegreeType()).isPresent(),
                        "degreeType", "m.validator.degreeType.invalid");
            } else {
                rejectIfFalse(errors, degreeTypeRepository.findById(degreeTypeDTO.getDegreeType()).isPresent(),
                        "degreeType", "m.validator.degreeType.invalid");
            }
        }

        return errors;
    }
}
