package bg.duosoft.uniapplicationdemo.services.impl;

import bg.duosoft.uniapplicationdemo.mappers.StudentsRequirementsResultsMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.StudentsRequirementsResultsDTO;
import bg.duosoft.uniapplicationdemo.models.entities.StudentsRequirementsResultsEntity;
import bg.duosoft.uniapplicationdemo.repositories.StudentsRequirementsResultsRepository;
import bg.duosoft.uniapplicationdemo.services.StudentsRequirementsResultsService;
import bg.duosoft.uniapplicationdemo.services.base.BaseServiceImpl;
import bg.duosoft.uniapplicationdemo.validators.StudentsRequirementsResultsValidator;
import org.springframework.stereotype.Service;

@Service
public class StudentsRequirementsResultsServiceImpl extends BaseServiceImpl<String, StudentsRequirementsResultsDTO, StudentsRequirementsResultsEntity, StudentsRequirementsResultsMapper, StudentsRequirementsResultsValidator, StudentsRequirementsResultsRepository> implements StudentsRequirementsResultsService {

    @Override
    public boolean isCachingEnabled() {
        return false;
    }
}
