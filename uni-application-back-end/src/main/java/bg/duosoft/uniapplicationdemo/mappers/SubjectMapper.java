package bg.duosoft.uniapplicationdemo.mappers;

import bg.duosoft.uniapplicationdemo.mappers.base.BaseObjectMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.SubjectDTO;
import bg.duosoft.uniapplicationdemo.models.entities.SubjectEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = TeacherMapper.class)
public abstract class SubjectMapper extends BaseObjectMapper<SubjectEntity, SubjectDTO> {
}
