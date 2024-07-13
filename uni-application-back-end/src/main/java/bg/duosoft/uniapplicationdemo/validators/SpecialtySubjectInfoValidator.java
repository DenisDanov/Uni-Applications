package bg.duosoft.uniapplicationdemo.validators;

import bg.duosoft.uniapplicationdemo.models.dtos.SpecialtySubjectInfoDTO;
import bg.duosoft.uniapplicationdemo.repositories.SpecialtySubjectInfoRepository;
import bg.duosoft.uniapplicationdemo.validators.config.ValidationError;
import bg.duosoft.uniapplicationdemo.validators.config.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SpecialtySubjectInfoValidator implements Validator<SpecialtySubjectInfoDTO> {

    @Override
    public List<ValidationError> validate(SpecialtySubjectInfoDTO specialtySubjectInfoDTO, Object... args) {
        List<ValidationError> errors = new ArrayList<>();

        Boolean isCreate = null;

        if (args != null && args.length > 0 && args[0] instanceof Boolean) {
            isCreate = (Boolean) args[0];
        }

        rejectIfEmpty(errors, specialtySubjectInfoDTO, "specialtySubjectInfoDTO", "m.validator.specialtySubjectInfoDTO.empty");

        if (errors.isEmpty()) {
            rejectIfEmpty(errors, specialtySubjectInfoDTO.getSpecialtyId(), "specialtyId", "m.validator.specialtyId.empty");
            rejectIfEmpty(errors, specialtySubjectInfoDTO.getSubjectId(), "subjectId", "m.validator.subjectId.empty");
            rejectIfEmpty(errors, specialtySubjectInfoDTO.getDurationHours(), "durationHours", "m.validator.durationHours.empty");
        }

        if (errors.isEmpty() && isCreate != null && isCreate) {
            SpecialtySubjectInfoRepository specialtySubjectInfoRepository = (SpecialtySubjectInfoRepository) args[1];
            rejectIfTrue(errors,specialtySubjectInfoRepository.findBySubjectId(specialtySubjectInfoDTO.getSpecialtyId(),specialtySubjectInfoDTO.getSubjectId()).isPresent(),"id","m.validator.id.exists");
        }

        return errors;
    }
}
