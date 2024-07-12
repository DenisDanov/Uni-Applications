package bg.duosoft.uniapplicationdemo.controllers;

import bg.duosoft.uniapplicationdemo.controllers.base.CrudController;
import bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures.ApplicationStatusDTO;
import bg.duosoft.uniapplicationdemo.services.ApplicationStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/application-status")
@RequiredArgsConstructor
public class ApplicationStatusController extends CrudController<String, ApplicationStatusDTO, ApplicationStatusService> {
}
