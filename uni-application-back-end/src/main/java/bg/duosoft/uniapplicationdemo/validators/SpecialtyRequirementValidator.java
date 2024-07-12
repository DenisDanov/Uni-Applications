package bg.duosoft.uniapplicationdemo.validators;

import bg.duosoft.uniapplicationdemo.models.dtos.SpecialtyRequirementDTO;
import bg.duosoft.uniapplicationdemo.validators.config.ValidationError;
import bg.duosoft.uniapplicationdemo.validators.config.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SpecialtyRequirementValidator implements Validator<SpecialtyRequirementDTO> {
    @Override
    public List<ValidationError> validate(SpecialtyRequirementDTO specialtyRequirementDTO, Object... args) {
        List<ValidationError> errors = new ArrayList<>();
        Boolean isCreate = null;

        if (args != null && args.length > 0 && args[0] instanceof Boolean) {
            isCreate = (Boolean) args[0];
        }

        rejectIfEmpty(errors, specialtyRequirementDTO, "specialtyRequirementDTO", "m.validator.specialtyRequirementDTO.empty");

        if (errors.isEmpty()) {
            rejectIfEmpty(errors, specialtyRequirementDTO.getMinGrade(), "minGrade", "m.validator.minGrade.empty");
            rejectIfEmptyString(errors, specialtyRequirementDTO.getRequirementDetails(), "requirementDetails", "m.validator.requirementDetails.empty");
            rejectIfEmpty(errors, specialtyRequirementDTO.getStandardizedTestMinResult(), "standardizedTestMinResult", "m.validator.standardizedTestMinResult.empty");
            rejectIfEmptyBoolean(errors, specialtyRequirementDTO.getLetterOfRecommendationRequired(), "letterOfRecommendationRequired", "m.validator.letterOfRecommendationRequired.empty");
            rejectIfEmptyBoolean(errors, specialtyRequirementDTO.getPersonalStatementRequired(), "personalStatementRequired", "m.validator.personalStatementRequired.empty");

            if (!hasValidationErrorPointer(errors, "minGrade")) {
                if (specialtyRequirementDTO.getMinGrade() < 2.0 || specialtyRequirementDTO.getMinGrade() > 6.0) {
                    reject(errors, "minGrade", "m.validator.minGpa.invalidRange");
                }
            }

            if (!hasValidationErrorPointer(errors, "standardizedTestMinResult")) {
                if (specialtyRequirementDTO.getStandardizedTestMinResult() < 0.0) {
                    reject(errors, "standardizedTestMinResult", "m.validator.standardizedTestMinResult.invalidRange");
                }
            }
        }

        if (errors.isEmpty() && isCreate != null && isCreate) {
            rejectIfFalse(errors, specialtyRequirementDTO.getId() != null, "id", "m.validator.id.not.empty");
        } else if (errors.isEmpty() && isCreate != null) {
            rejectIfTrue(errors, specialtyRequirementDTO.getId() == null, "id", "m.validator.id.empty");
        }

        return errors;
    }
}
