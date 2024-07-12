package bg.duosoft.uniapplicationdemo.services.impl;

import bg.duosoft.uniapplicationdemo.mappers.TeacherMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.TeacherDTO;
import bg.duosoft.uniapplicationdemo.models.entities.TeacherEntity;
import bg.duosoft.uniapplicationdemo.repositories.TeacherRepository;
import bg.duosoft.uniapplicationdemo.services.TeacherService;
import bg.duosoft.uniapplicationdemo.services.base.BaseServiceImpl;
import bg.duosoft.uniapplicationdemo.validators.TeacherValidator;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl extends BaseServiceImpl<Long, TeacherDTO, TeacherEntity, TeacherMapper, TeacherValidator, TeacherRepository> implements TeacherService {
}
