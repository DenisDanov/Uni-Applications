package bg.duosoft.uniapplicationdemo.validators;

import bg.duosoft.uniapplicationdemo.models.dtos.SubjectDTO;
import bg.duosoft.uniapplicationdemo.repositories.SubjectRepository;
import bg.duosoft.uniapplicationdemo.validators.config.ValidationError;
import bg.duosoft.uniapplicationdemo.validators.config.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubjectValidator implements Validator<SubjectDTO> {

    @Override
    public List<ValidationError> validate(SubjectDTO dto, Object... args) {
        List<ValidationError> errors = new ArrayList<>();

        Boolean isCreate = null;

        if (args != null && args.length > 0 && args[0] instanceof Boolean) {
            isCreate = (Boolean) args[0];
        }

        rejectIfEmpty(errors, dto, "subjectDTO", "m.validator.subjectDTO.empty");

        if (errors.isEmpty()) {
            rejectIfEmpty(errors, dto.getSubjectName(), "subjectName", "m.validator.subjectName.empty");
        }

        if (errors.isEmpty() && isCreate != null) {
            SubjectRepository subjectRepository = (SubjectRepository) args[1];
            rejectIfTrue(errors, subjectRepository.findBySubjectName(dto.getSubjectName()).isPresent(), "subjectName", "m.validator.subjectName.exists");
            rejectIfStringLengthBigger(errors,dto.getSubjectName(),255,"subjectName","m.validator.subjectName.length.tooLong");

            if (errors.isEmpty() && isCreate) {
                rejectIfTrue(errors, dto.getId() != null, "id", "m.validator.id.not.empty");
            } else if (errors.isEmpty()) {
                rejectIfTrue(errors, dto.getId() == null, "id", "m.validator.id.empty");
            }
        }

        return errors;
    }
}
