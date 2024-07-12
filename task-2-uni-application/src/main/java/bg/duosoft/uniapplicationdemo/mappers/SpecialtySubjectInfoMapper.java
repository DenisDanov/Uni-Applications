package bg.duosoft.uniapplicationdemo.mappers;

import bg.duosoft.uniapplicationdemo.mappers.base.BaseObjectMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.SpecialtySubjectInfoDTO;
import bg.duosoft.uniapplicationdemo.models.entities.SpecialtySubjectInfoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class SpecialtySubjectInfoMapper extends BaseObjectMapper<SpecialtySubjectInfoEntity, SpecialtySubjectInfoDTO> {

    @Mapping(target = "specialtyId", source = "id.specialtyId")
    @Mapping(target = "subjectId", source = "id.subjectId")
    public abstract SpecialtySubjectInfoDTO toDto(SpecialtySubjectInfoEntity e);

}
