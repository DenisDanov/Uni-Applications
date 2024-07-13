package bg.duosoft.uniapplicationdemo.mappers;

import bg.duosoft.uniapplicationdemo.mappers.base.BaseObjectMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.StudentsRequirementsResultsDTO;
import bg.duosoft.uniapplicationdemo.models.entities.StudentsRequirementsResultsEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class StudentsRequirementsResultsMapper extends BaseObjectMapper<StudentsRequirementsResultsEntity, StudentsRequirementsResultsDTO> {
}
