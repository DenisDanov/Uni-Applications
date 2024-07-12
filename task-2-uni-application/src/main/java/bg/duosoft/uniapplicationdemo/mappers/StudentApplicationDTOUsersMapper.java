package bg.duosoft.uniapplicationdemo.mappers;

import bg.duosoft.uniapplicationdemo.mappers.base.BaseObjectMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.StudentApplicationDTOUsers;
import bg.duosoft.uniapplicationdemo.models.entities.StudentApplicationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {StudentApplicationMapper.class})
public abstract class StudentApplicationDTOUsersMapper extends BaseObjectMapper<StudentApplicationEntity, StudentApplicationDTOUsers> {

    @Mapping(target = "username", source = "username")
    @Mapping(target = "specialtyId", source = "specialty.id")
    @Mapping(target = "facultyId", source = "faculty.id")
    @Mapping(target = "facultyName", source = "faculty.facultyName")
    @Mapping(target = "specialtyName", source = "specialty.specialtyName")
    public abstract StudentApplicationDTOUsers toDto(StudentApplicationEntity e);
}
