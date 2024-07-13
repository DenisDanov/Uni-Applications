package bg.duosoft.uniapplicationdemo.mappers;

import bg.duosoft.uniapplicationdemo.mappers.base.BaseObjectMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.DeclinedStudentApplicationDTO;
import bg.duosoft.uniapplicationdemo.models.entities.DeclinedStudentApplicationEntity;
import bg.duosoft.uniapplicationdemo.models.entities.StudentApplicationEntity;
import bg.duosoft.uniapplicationdemo.models.entities.compositeKeys.StudentsApplicationsId;
import bg.duosoft.uniapplicationdemo.repositories.StudentApplicationRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Mapper(componentModel = "spring", uses = {StudentApplicationMapper.class})
public abstract class DeclinedStudentApplicationMapper extends BaseObjectMapper<DeclinedStudentApplicationEntity, DeclinedStudentApplicationDTO> {

    @Autowired
    private StudentApplicationRepository studentApplicationRepository;

    @Mapping(target = "specialtyId", source = "id.specialtyId")
    @Mapping(target = "facultyId", source = "id.facultyId")
    @Mapping(target = "deleteDate", source = "deleteDate")
    public abstract DeclinedStudentApplicationDTO toDto(DeclinedStudentApplicationEntity e);

    @Mapping(target = "id.username", source = "username")
    @Mapping(target = "id.specialtyId", source = "specialtyId")
    @Mapping(target = "id.facultyId", source = "facultyId")
    @Mapping(target = "deleteDate", source = "deleteDate")
    public abstract DeclinedStudentApplicationEntity toEntity(DeclinedStudentApplicationDTO dto);

    @AfterMapping
    protected void afterToEntityMapping(DeclinedStudentApplicationDTO dto,
                                        @MappingTarget DeclinedStudentApplicationEntity entity) {
        Optional<StudentApplicationEntity> byId = studentApplicationRepository.findById(new StudentsApplicationsId(dto.getUsername(), dto.getSpecialtyId(), dto.getFacultyId()));
        entity.setDeleteDate(dto.getDeleteDate());
        byId.ifPresent(entity::setStudentApplication);
    }
}
