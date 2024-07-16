package bg.duosoft.uniapplicationdemo.services;

import bg.duosoft.uniapplicationdemo.models.dtos.StudentsRequirementsResultsDTO;
import bg.duosoft.uniapplicationdemo.services.base.BaseService;

public interface StudentsRequirementsResultsService extends BaseService<String, StudentsRequirementsResultsDTO> {

    void processTestResults(String jsonString);

    @Override
    default boolean isCachingEnabled() {
        return false;
    }
}
