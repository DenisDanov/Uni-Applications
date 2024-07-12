package bg.duosoft.uniapplicationdemo.controllers;

import bg.duosoft.uniapplicationdemo.controllers.base.CrudController;
import bg.duosoft.uniapplicationdemo.models.dtos.SpecialtyProgramDTO;
import bg.duosoft.uniapplicationdemo.services.SpecialtyProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/specialty-program")
@RequiredArgsConstructor
public class SpecialtyProgramController extends CrudController<Long, SpecialtyProgramDTO, SpecialtyProgramService> {
}
