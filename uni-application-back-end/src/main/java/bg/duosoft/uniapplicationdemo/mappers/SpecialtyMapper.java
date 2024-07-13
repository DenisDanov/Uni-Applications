package bg.duosoft.uniapplicationdemo.mappers;

import bg.duosoft.uniapplicationdemo.mappers.base.BaseObjectMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.SpecialtyDTO;
import bg.duosoft.uniapplicationdemo.models.entities.SpecialtyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SpecialtyRequirementMapper.class, SpecialtyProgramMapper.class, SubjectMapper.class})
public abstract class SpecialtyMapper extends BaseObjectMapper<SpecialtyEntity, SpecialtyDTO> {

    @Mapping(target = "facultyID", source = "faculty.id")
    public abstract SpecialtyDTO toDto(SpecialtyEntity entity);

}
