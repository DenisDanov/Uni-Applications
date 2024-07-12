package bg.duosoft.uniapplicationdemo.controllers;

import bg.duosoft.uniapplicationdemo.controllers.base.CrudController;
import bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures.DegreeTypeDTO;
import bg.duosoft.uniapplicationdemo.services.DegreeTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/degree-type")
@RequiredArgsConstructor
public class DegreeTypeController extends CrudController<String, DegreeTypeDTO, DegreeTypeService> {
}
