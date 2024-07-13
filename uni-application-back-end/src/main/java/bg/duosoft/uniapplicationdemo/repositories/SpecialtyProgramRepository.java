package bg.duosoft.uniapplicationdemo.repositories;

import bg.duosoft.uniapplicationdemo.models.entities.SpecialtyProgramEntity;
import bg.duosoft.uniapplicationdemo.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialtyProgramRepository extends BaseRepository<SpecialtyProgramEntity, Long> {
}
