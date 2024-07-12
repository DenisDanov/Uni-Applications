package bg.duosoft.uniapplicationdemo.controllers;

import bg.duosoft.uniapplicationdemo.controllers.base.CrudController;
import bg.duosoft.uniapplicationdemo.models.dtos.SpecialtyRequirementDTO;
import bg.duosoft.uniapplicationdemo.services.SpecialtyRequirementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/specialty-requirement")
@RequiredArgsConstructor
public class SpecialtyRequirementController extends CrudController<Long, SpecialtyRequirementDTO, SpecialtyRequirementService> {
}
