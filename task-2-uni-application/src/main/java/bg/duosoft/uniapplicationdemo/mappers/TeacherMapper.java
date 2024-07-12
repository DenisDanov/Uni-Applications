package bg.duosoft.uniapplicationdemo.mappers;

import bg.duosoft.uniapplicationdemo.mappers.base.BaseObjectMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.TeacherDTO;
import bg.duosoft.uniapplicationdemo.models.entities.TeacherEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class TeacherMapper extends BaseObjectMapper<TeacherEntity, TeacherDTO> {
}
