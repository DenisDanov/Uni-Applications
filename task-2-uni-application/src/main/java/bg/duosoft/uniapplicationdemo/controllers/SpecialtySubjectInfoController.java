package bg.duosoft.uniapplicationdemo.controllers;

import bg.duosoft.uniapplicationdemo.controllers.base.CrudController;
import bg.duosoft.uniapplicationdemo.models.dtos.SpecialtySubjectInfoDTO;
import bg.duosoft.uniapplicationdemo.models.entities.compositeKeys.SpecialtiesSubjectsId;
import bg.duosoft.uniapplicationdemo.services.SpecialtySubjectInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/specialty-subject-info")
@RequiredArgsConstructor
public class SpecialtySubjectInfoController extends CrudController<SpecialtiesSubjectsId, SpecialtySubjectInfoDTO, SpecialtySubjectInfoService> {
}
