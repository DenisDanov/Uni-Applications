package bg.duosoft.uniapplicationdemo.repositories;

import bg.duosoft.uniapplicationdemo.models.entities.nomenclatures.AccreditationStatusEntity;
import bg.duosoft.uniapplicationdemo.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccreditationStatusRepository extends BaseRepository<AccreditationStatusEntity, String> {
    Optional<AccreditationStatusEntity> findByAccreditationType(String accreditationType);
}
