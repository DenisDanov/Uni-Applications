package bg.duosoft.uniapplicationdemo.validators;

import bg.duosoft.uniapplicationdemo.models.dtos.StudentApplicationDTO;
import bg.duosoft.uniapplicationdemo.models.entities.FacultyEntity;
import bg.duosoft.uniapplicationdemo.models.entities.SpecialtyEntity;
import bg.duosoft.uniapplicationdemo.models.entities.SpecialtyRequirementEntity;
import bg.duosoft.uniapplicationdemo.repositories.FacultyRepository;
import bg.duosoft.uniapplicationdemo.repositories.SpecialtyRepository;
import bg.duosoft.uniapplicationdemo.repositories.StudentApplicationRepository;
import bg.duosoft.uniapplicationdemo.services.UserService;
import bg.duosoft.uniapplicationdemo.validators.config.ValidationError;
import bg.duosoft.uniapplicationdemo.validators.config.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class StudentApplicationValidator implements Validator<StudentApplicationDTO> {

    private final UserService userService;

    private final SpecialtyRepository specialtyRepository;

    private final FacultyRepository facultyRepository;

    @Override
    public List<ValidationError> validate(StudentApplicationDTO applicationDTO, Object... args) {
        List<ValidationError> errors = new ArrayList<>();

        Boolean isCreate = null;

        if (args != null && args.length > 0 && args[0] instanceof Boolean) {
            isCreate = (Boolean) args[0];
        }

        rejectIfEmpty(errors, applicationDTO, "applicationDTO", "The application data is missing.");

        if (errors.isEmpty()) {
            rejectIfEmptyString(errors, applicationDTO.getUsername(), "username", "The username cannot be empty.");
            rejectIfEmpty(errors, applicationDTO.getSpecialtyId(), "specialtyId", "The specialty ID cannot be empty.");
            rejectIfEmpty(errors, applicationDTO.getFacultyId(), "facultyId", "The faculty ID cannot be empty.");
            rejectIfEmptyDate(errors, applicationDTO.getApplicationSentDate(), "applicationSentDate", "The application sent date cannot be empty.");
            rejectIfEmptyString(errors, applicationDTO.getApplicationDescription(), "applicationDescription", "The application description cannot be empty.");
            rejectIfEmpty(errors, applicationDTO.getAvgGrade(), "avgGrade", "The average grade cannot be empty.");
            rejectIfEmpty(errors, applicationDTO.getStandardizedTestResult(), "standardizedTestResult", "The standardized test result cannot be empty.");
            rejectIfTrue(errors, applicationDTO.getStandardizedTestResult() < 0, "standardizedTestResult", "The standardized test result cannot be negative");
            rejectIfTrue(errors, userService.findByUsername(applicationDTO.getUsername()).isEmpty(), "username", "The username does not exist.");
            rejectIfTrue(errors, applicationDTO.getAvgGrade() < 3 || applicationDTO.getAvgGrade() > 6, "avgGrade", "The average grade is invalid.");

            Optional<SpecialtyEntity> specialty = specialtyRepository.findById(applicationDTO.getSpecialtyId());
            Optional<FacultyEntity> faculty = facultyRepository.findById(applicationDTO.getFacultyId());

            rejectIfTrue(errors, specialty.isEmpty(), "specialty", "The specified specialty does not exist.");
            rejectIfTrue(errors, faculty.isEmpty(), "faculty", "The specified faculty does not exist.");
            if (specialty.isPresent()) {
                SpecialtyRequirementEntity requirementEntity = specialty.get().getSpecialtyRequirement();
                if (requirementEntity.getPersonalStatementRequired()) {
                    rejectIfEmptyString(errors, applicationDTO.getPersonalStatement(), "personalStatement", "The personal statement cannot be empty.");
                }
                if (requirementEntity.getLetterOfRecommendationRequired()) {
                    rejectIfEmptyString(errors, applicationDTO.getLetterOfRecommendation(), "letterOfRecommendation", "The letter of recommendation cannot be empty.");
                }
                if (requirementEntity.getLanguageProficiencyTestMinResult() != null) {
                    rejectIfTrue(errors, applicationDTO.getLanguageProficiencyTestResult() == null, "languageProficiencyTestResult", "The language proficiency test result is invalid.");
                    rejectIfTrue(errors, applicationDTO.getLanguageProficiencyTestResult() < 0, "languageProficiencyTestResult", "The language proficiency test result is invalid.");
                }
            }

            if (specialty.isPresent() && faculty.isPresent() && !faculty.get().getSpecialties().contains(specialty.get())) {
                reject(errors, "specialtyId", "The specified specialty does not belong to the selected faculty.");
            }
        }

        if (errors.isEmpty() && isCreate != null && isCreate) {
            StudentApplicationRepository studentApplicationRepository = (StudentApplicationRepository) args[1];
            rejectIfTrue(errors, studentApplicationRepository.findByStudentsApplicationsId(
                    applicationDTO.getUsername(),
                    applicationDTO.getFacultyId(),
                    applicationDTO.getSpecialtyId()).isPresent(), "id", "An application with the same details already exists.");
        }

        return errors;
    }
}
