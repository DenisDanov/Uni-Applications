package bg.duosoft.uniapplicationdemo.mappers;

import bg.duosoft.uniapplicationdemo.mappers.base.BaseObjectMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.FacultyDTO;
import bg.duosoft.uniapplicationdemo.models.entities.FacultyEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {SpecialtyMapper.class, TeacherMapper.class})
public abstract class FacultyMapper extends BaseObjectMapper<FacultyEntity, FacultyDTO> {
}
