package bg.duosoft.uniapplicationdemo.controllers;

import bg.duosoft.uniapplicationdemo.controllers.base.CrudController;
import bg.duosoft.uniapplicationdemo.models.dtos.FacultyDTO;
import bg.duosoft.uniapplicationdemo.services.FacultyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/faculty")
@RequiredArgsConstructor
public class FacultyController extends CrudController<Long, FacultyDTO, FacultyService> {

    @Override
    public List<FacultyDTO> getAll() {
        return super.getService().getAll();
    }
}
