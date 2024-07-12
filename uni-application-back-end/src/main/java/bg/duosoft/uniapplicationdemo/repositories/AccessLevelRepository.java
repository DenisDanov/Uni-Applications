package bg.duosoft.uniapplicationdemo.repositories;

import bg.duosoft.uniapplicationdemo.models.entities.nomenclatures.AccessLevelEntity;
import bg.duosoft.uniapplicationdemo.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccessLevelRepository extends BaseRepository<AccessLevelEntity, String> {
    Optional<AccessLevelEntity> findByAccessType(String accessLevel);
}
