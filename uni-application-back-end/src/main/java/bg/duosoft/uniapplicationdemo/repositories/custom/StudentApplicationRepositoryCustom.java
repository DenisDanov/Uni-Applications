package bg.duosoft.uniapplicationdemo.repositories.custom;

import bg.duosoft.uniapplicationdemo.models.dtos.FilterStudentApplicationsDTO;
import bg.duosoft.uniapplicationdemo.models.entities.StudentApplicationEntity;

import java.util.List;

public interface StudentApplicationRepositoryCustom {
    List<StudentApplicationEntity> searchRecords(FilterStudentApplicationsDTO filter);
}
