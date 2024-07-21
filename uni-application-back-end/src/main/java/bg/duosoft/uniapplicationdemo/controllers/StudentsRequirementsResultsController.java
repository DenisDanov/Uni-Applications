package bg.duosoft.uniapplicationdemo.controllers;

import bg.duosoft.uniapplicationdemo.controllers.base.CrudController;
import bg.duosoft.uniapplicationdemo.models.dtos.StudentsRequirementsResultsDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.TestResultsDTO;
import bg.duosoft.uniapplicationdemo.services.StudentsRequirementsResultsService;
import bg.duosoft.uniapplicationdemo.services.impl.TestStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/students-requirements-results")
@RequiredArgsConstructor
public class StudentsRequirementsResultsController extends CrudController<String, StudentsRequirementsResultsDTO, StudentsRequirementsResultsService> {

    private final TestStateService testStateService;

    @Override
    public StudentsRequirementsResultsDTO getById(String id) {
        StudentsRequirementsResultsDTO resultsDTO = super.getService().getById(id);
        return resultsDTO == null ? new StudentsRequirementsResultsDTO() : resultsDTO;
    }

    @PostMapping("/submit-test")
    public ResponseEntity<StudentsRequirementsResultsDTO> submitTestResults(@RequestBody TestResultsDTO testResultsDTO) {
        testStateService.deleteTestState(testResultsDTO.getUsername());
        StudentsRequirementsResultsDTO resultsDTO = super.getService().getById(testResultsDTO.getUsername());
        if (resultsDTO != null) {
            if (testResultsDTO.getTestName().equals("language")) {
                resultsDTO.setLanguageProficiencyTestResult((double) (testResultsDTO.getResult() * 10));
            } else {
                resultsDTO.setStandardizedTestResult((double) (testResultsDTO.getResult() * 10));
            }

            super.getService().update(resultsDTO);
        } else {
            return ResponseEntity.ok().body(super.getService().create(new StudentsRequirementsResultsDTO(testResultsDTO.getUsername(),
                    testResultsDTO.getTestName().equals("language") ? Double.parseDouble(String.valueOf(testResultsDTO.getResult() * 10)) : null,
                    testResultsDTO.getTestName().equals("standard") ? Double.parseDouble(String.valueOf(testResultsDTO.getResult() * 10)) : null)));
        }
        return ResponseEntity.ok().body(resultsDTO);
    }
}
