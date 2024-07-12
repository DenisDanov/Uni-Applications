package bg.duosoft.uniapplicationdemo.services;

import bg.duosoft.uniapplicationdemo.models.dtos.SpecialtyDTO;
import bg.duosoft.uniapplicationdemo.models.entities.SpecialtyEntity;
import bg.duosoft.uniapplicationdemo.services.base.BaseService;

import java.util.List;
import java.util.Optional;

public interface SpecialtyService extends BaseService<Long, SpecialtyDTO> {
    Optional<SpecialtyDTO> findBySpecialtyName(String name);

    List<SpecialtyDTO> findByFacultyId(Long facultyId);
}
