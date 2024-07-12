package bg.duosoft.uniapplicationdemo.services.impl;

import bg.duosoft.uniapplicationdemo.mappers.SpecialtyProgramMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.SpecialtyProgramDTO;
import bg.duosoft.uniapplicationdemo.models.entities.SpecialtyProgramEntity;
import bg.duosoft.uniapplicationdemo.repositories.SpecialtyProgramRepository;
import bg.duosoft.uniapplicationdemo.services.SpecialtyProgramService;
import bg.duosoft.uniapplicationdemo.services.base.BaseServiceImpl;
import bg.duosoft.uniapplicationdemo.validators.SpecialtyProgramValidator;
import org.springframework.stereotype.Service;

@Service
public class SpecialtyProgramServiceImpl extends BaseServiceImpl<Long, SpecialtyProgramDTO, SpecialtyProgramEntity, SpecialtyProgramMapper, SpecialtyProgramValidator, SpecialtyProgramRepository> implements SpecialtyProgramService {
}
