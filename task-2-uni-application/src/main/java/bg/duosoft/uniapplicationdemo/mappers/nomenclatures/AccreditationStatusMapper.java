package bg.duosoft.uniapplicationdemo.mappers.nomenclatures;

import bg.duosoft.uniapplicationdemo.mappers.base.BaseObjectMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures.AccreditationStatusDTO;
import bg.duosoft.uniapplicationdemo.models.entities.nomenclatures.AccreditationStatusEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class AccreditationStatusMapper extends BaseObjectMapper<AccreditationStatusEntity,AccreditationStatusDTO> {
}
