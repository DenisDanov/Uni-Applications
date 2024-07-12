package bg.duosoft.uniapplicationdemo.mappers.nomenclatures;

import bg.duosoft.uniapplicationdemo.mappers.base.BaseObjectMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures.DegreeTypeDTO;
import bg.duosoft.uniapplicationdemo.models.entities.nomenclatures.DegreeTypeEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class DegreeTypeMapper extends BaseObjectMapper<DegreeTypeEntity, DegreeTypeDTO> {
}
