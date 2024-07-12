package bg.duosoft.uniapplicationdemo.validators;

import bg.duosoft.uniapplicationdemo.models.dtos.TeacherDTO;
import bg.duosoft.uniapplicationdemo.repositories.SubjectRepository;
import bg.duosoft.uniapplicationdemo.validators.config.ValidationError;
import bg.duosoft.uniapplicationdemo.validators.config.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TeacherValidator implements Validator<TeacherDTO> {

    private final SubjectValidator subjectValidator;

    @Override
    public List<ValidationError> validate(TeacherDTO dto, Object... args) {
        List<ValidationError> errors = new ArrayList<>();

        Boolean isCreate = null;

        if (args != null && args.length > 0 && args[0] instanceof Boolean) {
            isCreate = (Boolean) args[0];
        }

        rejectIfEmpty(errors, dto, "teacherDTO", "m.validator.teacherDTO.empty");

        if (errors.isEmpty()) {
            rejectIfEmpty(errors, dto.getTeacherName(), "subjectName", "m.validator.subjectName.empty");
            rejectIfStringLengthBigger(errors,dto.getTeacherName(),255,"teacherName","m.validator.teacherName.length.tooLong");
        }

        if (!dto.getSubjects().isEmpty()) {
            dto.getSubjects().forEach(subject -> errors.addAll(subjectValidator.validate(subject)));
        }

        if (isCreate != null && isCreate && errors.isEmpty()) {
            rejectIfTrue(errors, dto.getId() != null, "m.validator.subject.id", "m.validator.subject.id.not.empty");
        } else if (isCreate != null && errors.isEmpty()) {
            rejectIfTrue(errors, dto.getId() == null, "m.validator.subject.id", "m.validator.subject.id.empty");

        }

        return errors;
    }
}
