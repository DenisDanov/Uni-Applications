package bg.duosoft.uniapplicationdemo.services.impl;

import bg.duosoft.uniapplicationdemo.mappers.SpecialtyRequirementMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.SpecialtyRequirementDTO;
import bg.duosoft.uniapplicationdemo.models.entities.SpecialtyRequirementEntity;
import bg.duosoft.uniapplicationdemo.repositories.SpecialtyRequirementRepository;
import bg.duosoft.uniapplicationdemo.services.SpecialtyRequirementService;
import bg.duosoft.uniapplicationdemo.services.base.BaseServiceImpl;
import bg.duosoft.uniapplicationdemo.validators.SpecialtyRequirementValidator;
import org.springframework.stereotype.Service;

@Service
public class SpecialtyRequirementServiceImpl extends BaseServiceImpl<Long, SpecialtyRequirementDTO, SpecialtyRequirementEntity, SpecialtyRequirementMapper, SpecialtyRequirementValidator, SpecialtyRequirementRepository> implements SpecialtyRequirementService {
}
