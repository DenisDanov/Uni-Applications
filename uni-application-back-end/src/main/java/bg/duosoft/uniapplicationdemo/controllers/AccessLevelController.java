package bg.duosoft.uniapplicationdemo.controllers;

import bg.duosoft.uniapplicationdemo.controllers.base.CrudController;
import bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures.AccessLevelDTO;
import bg.duosoft.uniapplicationdemo.services.AccessLevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/access-level")
@RequiredArgsConstructor
public class AccessLevelController extends CrudController<String, AccessLevelDTO, AccessLevelService> {
}
