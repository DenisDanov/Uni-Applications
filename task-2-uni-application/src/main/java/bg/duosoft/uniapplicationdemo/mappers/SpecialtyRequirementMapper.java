package bg.duosoft.uniapplicationdemo.mappers;

import bg.duosoft.uniapplicationdemo.mappers.base.BaseObjectMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.SpecialtyRequirementDTO;
import bg.duosoft.uniapplicationdemo.models.entities.SpecialtyRequirementEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class SpecialtyRequirementMapper extends BaseObjectMapper<SpecialtyRequirementEntity, SpecialtyRequirementDTO> {
}
