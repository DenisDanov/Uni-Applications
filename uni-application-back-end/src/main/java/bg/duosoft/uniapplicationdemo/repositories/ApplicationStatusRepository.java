package bg.duosoft.uniapplicationdemo.repositories;

import bg.duosoft.uniapplicationdemo.models.entities.nomenclatures.ApplicationStatusEntity;
import bg.duosoft.uniapplicationdemo.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationStatusRepository extends BaseRepository<ApplicationStatusEntity, String> {
    Optional<ApplicationStatusEntity> findByApplicationStatus(String status);
}
