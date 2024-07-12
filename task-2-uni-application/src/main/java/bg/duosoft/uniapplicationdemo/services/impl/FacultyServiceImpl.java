package bg.duosoft.uniapplicationdemo.services.impl;

import bg.duosoft.uniapplicationdemo.mappers.FacultyMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.FacultyDTO;
import bg.duosoft.uniapplicationdemo.models.entities.FacultyEntity;
import bg.duosoft.uniapplicationdemo.repositories.FacultyRepository;
import bg.duosoft.uniapplicationdemo.services.FacultyService;
import bg.duosoft.uniapplicationdemo.services.base.BaseServiceImpl;
import bg.duosoft.uniapplicationdemo.validators.FacultyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FacultyServiceImpl extends BaseServiceImpl<Long, FacultyDTO, FacultyEntity, FacultyMapper, FacultyValidator, FacultyRepository> implements FacultyService {

    @Override
    public FacultyDTO findFacultyBySpecialty(String specialty) {
        return super.getMapper().toDto(super.getRepository().findBySpecialtyName(specialty));
    }
}
