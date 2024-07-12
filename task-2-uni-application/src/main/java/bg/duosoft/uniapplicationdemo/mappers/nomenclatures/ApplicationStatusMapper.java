package bg.duosoft.uniapplicationdemo.mappers.nomenclatures;

import bg.duosoft.uniapplicationdemo.mappers.base.BaseObjectMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures.ApplicationStatusDTO;
import bg.duosoft.uniapplicationdemo.models.entities.nomenclatures.ApplicationStatusEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ApplicationStatusMapper extends BaseObjectMapper<ApplicationStatusEntity, ApplicationStatusDTO> {
}
