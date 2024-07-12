package bg.duosoft.uniapplicationdemo.repositories;

import bg.duosoft.uniapplicationdemo.models.entities.SpecialtyRequirementEntity;
import bg.duosoft.uniapplicationdemo.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialtyRequirementRepository extends BaseRepository<SpecialtyRequirementEntity, Long> {
}
