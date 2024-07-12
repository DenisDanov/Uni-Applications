package bg.duosoft.uniapplicationdemo.mappers.nomenclatures;

import bg.duosoft.uniapplicationdemo.mappers.base.BaseObjectMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures.RoleDTO;
import bg.duosoft.uniapplicationdemo.models.entities.nomenclatures.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class RoleMapper extends BaseObjectMapper<RoleEntity, RoleDTO> {
}
