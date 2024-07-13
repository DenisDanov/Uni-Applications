package bg.duosoft.uniapplicationdemo.services.impl;

import bg.duosoft.uniapplicationdemo.mappers.nomenclatures.DegreeTypeMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures.DegreeTypeDTO;
import bg.duosoft.uniapplicationdemo.models.entities.nomenclatures.DegreeTypeEntity;
import bg.duosoft.uniapplicationdemo.repositories.DegreeTypeRepository;
import bg.duosoft.uniapplicationdemo.services.DegreeTypeService;
import bg.duosoft.uniapplicationdemo.services.base.BaseServiceImpl;
import bg.duosoft.uniapplicationdemo.validators.nomenclatures.DegreeTypeValidator;
import org.springframework.stereotype.Service;

@Service
public class DegreeTypeServiceImpl extends BaseServiceImpl<String,DegreeTypeDTO, DegreeTypeEntity, DegreeTypeMapper, DegreeTypeValidator, DegreeTypeRepository> implements DegreeTypeService {
}
