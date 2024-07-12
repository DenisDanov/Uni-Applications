package bg.duosoft.uniapplicationdemo.services.impl;

import bg.duosoft.uniapplicationdemo.mappers.nomenclatures.ApplicationStatusMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures.ApplicationStatusDTO;
import bg.duosoft.uniapplicationdemo.models.entities.nomenclatures.ApplicationStatusEntity;
import bg.duosoft.uniapplicationdemo.repositories.ApplicationStatusRepository;
import bg.duosoft.uniapplicationdemo.services.ApplicationStatusService;
import bg.duosoft.uniapplicationdemo.services.base.BaseServiceImpl;
import bg.duosoft.uniapplicationdemo.validators.nomenclatures.ApplicationStatusValidator;
import org.springframework.stereotype.Service;

@Service
public class ApplicationStatusServiceImpl extends BaseServiceImpl<String,ApplicationStatusDTO, ApplicationStatusEntity, ApplicationStatusMapper, ApplicationStatusValidator, ApplicationStatusRepository> implements ApplicationStatusService {
}
