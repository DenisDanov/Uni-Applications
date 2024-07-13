package bg.duosoft.uniapplicationdemo.controllers;

import bg.duosoft.uniapplicationdemo.controllers.base.CrudController;
import bg.duosoft.uniapplicationdemo.models.dtos.StudentsRequirementsResultsDTO;
import bg.duosoft.uniapplicationdemo.services.StudentsRequirementsResultsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/students-requirements-results")
@RequiredArgsConstructor
public class StudentsRequirementsResultsController extends CrudController<String, StudentsRequirementsResultsDTO, StudentsRequirementsResultsService> {

    @Override
    public StudentsRequirementsResultsDTO getById(String id) {
        StudentsRequirementsResultsDTO resultsDTO = super.getService().getById(id);
        return resultsDTO == null ? new StudentsRequirementsResultsDTO() : resultsDTO;
    }
}
