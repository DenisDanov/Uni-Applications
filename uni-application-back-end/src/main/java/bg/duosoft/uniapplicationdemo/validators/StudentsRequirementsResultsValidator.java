package bg.duosoft.uniapplicationdemo.validators;

import bg.duosoft.uniapplicationdemo.models.dtos.StudentsRequirementsResultsDTO;
import bg.duosoft.uniapplicationdemo.repositories.StudentsRequirementsResultsRepository;
import bg.duosoft.uniapplicationdemo.validators.config.ValidationError;
import bg.duosoft.uniapplicationdemo.validators.config.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StudentsRequirementsResultsValidator implements Validator<StudentsRequirementsResultsDTO> {

    private final StudentsRequirementsResultsRepository studentsRequirementsResultsRepository;

    @Override
    public List<ValidationError> validate(StudentsRequirementsResultsDTO dto, Object... args) {
        List<ValidationError> errors = new ArrayList<>();

        Boolean isCreate = null;

        if (args != null && args.length > 0 && args[0] instanceof Boolean) {
            isCreate = (Boolean) args[0];
        }

        rejectIfEmpty(errors, dto, "studentsRequirementsResultsDTO", "No data available, DTO is NULL.");
        rejectIfEmptyString(errors, dto.getUsername(), "username", "Username is empty.");

        if(errors.isEmpty()){
            if (dto.getStandardizedTestResult() != null) {
                rejectIfTrue(errors, dto.getStandardizedTestResult() < 0, "standardizedTestResult",
                        "Standardized test result is less than 0.");
            }
            if (dto.getLanguageProficiencyTestResult() != null) {
                rejectIfTrue(errors,  dto.getLanguageProficiencyTestResult() < 0, "languageProficiencyTestResult",
                        "Language proficiency test result is less than 0.");
            }
        }

        if (errors.isEmpty() && isCreate != null && isCreate) {
            rejectIfTrue(errors, studentsRequirementsResultsRepository.findById(dto.getUsername()).isPresent(), "username", "Test results for this user already exist.");
        }

        return errors;
    }
}
