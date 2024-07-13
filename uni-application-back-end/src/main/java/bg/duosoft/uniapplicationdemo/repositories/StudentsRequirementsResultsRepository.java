package bg.duosoft.uniapplicationdemo.repositories;

import bg.duosoft.uniapplicationdemo.models.entities.StudentsRequirementsResultsEntity;
import bg.duosoft.uniapplicationdemo.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentsRequirementsResultsRepository extends BaseRepository<StudentsRequirementsResultsEntity, String> {
}
