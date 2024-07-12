package bg.duosoft.uniapplicationdemo.mappers;

import bg.duosoft.uniapplicationdemo.mappers.base.BaseObjectMapper;
import bg.duosoft.uniapplicationdemo.mappers.nomenclatures.ApplicationStatusMapper;
import bg.duosoft.uniapplicationdemo.mappers.nomenclatures.RoleMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.StudentApplicationDTO;
import bg.duosoft.uniapplicationdemo.models.entities.FacultyEntity;
import bg.duosoft.uniapplicationdemo.models.entities.SpecialtyEntity;
import bg.duosoft.uniapplicationdemo.models.entities.StudentApplicationEntity;
import bg.duosoft.uniapplicationdemo.repositories.ApplicationStatusRepository;
import bg.duosoft.uniapplicationdemo.repositories.FacultyRepository;
import bg.duosoft.uniapplicationdemo.repositories.SpecialtyRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {SpecialtyMapper.class, FacultyMapper.class, ApplicationStatusMapper.class, RoleMapper.class})
public abstract class StudentApplicationMapper extends BaseObjectMapper<StudentApplicationEntity, StudentApplicationDTO> {

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Autowired
    private ApplicationStatusRepository applicationStatusRepository;

    @Mapping(target = "username", source = "username")
    @Mapping(target = "specialtyId", source = "specialty.id")
    @Mapping(target = "facultyId", source = "faculty.id")
    public abstract StudentApplicationDTO toDto(StudentApplicationEntity e);

    @Mapping(target = "id.username", source = "username")
    public abstract StudentApplicationEntity toEntity(StudentApplicationDTO dto);

    @AfterMapping
    protected void afterToEntityMapping(StudentApplicationDTO dto,
                                        @MappingTarget StudentApplicationEntity entity) {
        FacultyEntity faculty = facultyRepository.findById(dto.getFacultyId()).get();
        SpecialtyEntity specialtyEntity = specialtyRepository.findById(dto.getSpecialtyId()).get();
        entity.getId().setFacultyId(faculty.getId());
        entity.getId().setSpecialtyId(specialtyEntity.getId());
        entity.setFaculty(faculty);
        entity.setSpecialty(specialtyEntity);

        if (entity.getApplicationStatus() == null) {
            entity.setApplicationStatus(applicationStatusRepository.findByApplicationStatus("PENDING").get());
        }
    }
}
