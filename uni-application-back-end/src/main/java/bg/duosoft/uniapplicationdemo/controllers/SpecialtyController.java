package bg.duosoft.uniapplicationdemo.controllers;

import bg.duosoft.uniapplicationdemo.controllers.base.CrudController;
import bg.duosoft.uniapplicationdemo.models.dtos.SpecialtyDTO;
import bg.duosoft.uniapplicationdemo.services.SpecialtyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/specialty")
@RequiredArgsConstructor
public class SpecialtyController extends CrudController<Long, SpecialtyDTO, SpecialtyService> {

    @Override
    public List<SpecialtyDTO> getAll() {
        return super.getService().getAll();
    }

    @GetMapping("/by-faculty")
    public List<SpecialtyDTO> findByFacultyId(@RequestParam Long facultyId) {
        return super.getService().findByFacultyId(facultyId);
    }
}
