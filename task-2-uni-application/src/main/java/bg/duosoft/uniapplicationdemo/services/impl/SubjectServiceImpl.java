package bg.duosoft.uniapplicationdemo.services.impl;

import bg.duosoft.uniapplicationdemo.mappers.SubjectMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.SubjectDTO;
import bg.duosoft.uniapplicationdemo.models.entities.SubjectEntity;
import bg.duosoft.uniapplicationdemo.repositories.SubjectRepository;
import bg.duosoft.uniapplicationdemo.services.SubjectService;
import bg.duosoft.uniapplicationdemo.services.base.BaseServiceImpl;
import bg.duosoft.uniapplicationdemo.validators.SubjectValidator;
import org.springframework.stereotype.Service;

@Service
public class SubjectServiceImpl extends BaseServiceImpl<Long, SubjectDTO, SubjectEntity, SubjectMapper, SubjectValidator, SubjectRepository> implements SubjectService {
}
