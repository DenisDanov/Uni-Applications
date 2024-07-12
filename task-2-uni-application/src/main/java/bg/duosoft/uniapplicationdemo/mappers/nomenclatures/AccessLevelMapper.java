package bg.duosoft.uniapplicationdemo.mappers.nomenclatures;

import bg.duosoft.uniapplicationdemo.mappers.base.BaseObjectMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures.AccessLevelDTO;
import bg.duosoft.uniapplicationdemo.models.entities.nomenclatures.AccessLevelEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class AccessLevelMapper extends BaseObjectMapper<AccessLevelEntity, AccessLevelDTO> {
}
