package bg.duosoft.uniapplicationdemo.mappers;

import bg.duosoft.uniapplicationdemo.mappers.base.BaseObjectMapper;
import bg.duosoft.uniapplicationdemo.mappers.nomenclatures.AccreditationStatusMapper;
import bg.duosoft.uniapplicationdemo.mappers.nomenclatures.DegreeTypeMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.SpecialtyProgramDTO;
import bg.duosoft.uniapplicationdemo.models.entities.SpecialtyProgramEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DegreeTypeMapper.class, AccreditationStatusMapper.class})
public abstract class SpecialtyProgramMapper extends BaseObjectMapper<SpecialtyProgramEntity, SpecialtyProgramDTO> {
}
