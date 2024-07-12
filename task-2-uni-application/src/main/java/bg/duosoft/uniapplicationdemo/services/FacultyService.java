package bg.duosoft.uniapplicationdemo.services;

import bg.duosoft.uniapplicationdemo.models.dtos.FacultyDTO;
import bg.duosoft.uniapplicationdemo.services.base.BaseService;

public interface FacultyService extends BaseService<Long, FacultyDTO> {
    FacultyDTO findFacultyBySpecialty(String specialty);
}
