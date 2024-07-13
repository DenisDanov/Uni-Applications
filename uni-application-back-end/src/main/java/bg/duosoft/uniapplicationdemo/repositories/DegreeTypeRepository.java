package bg.duosoft.uniapplicationdemo.repositories;

import bg.duosoft.uniapplicationdemo.models.entities.nomenclatures.DegreeTypeEntity;
import bg.duosoft.uniapplicationdemo.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DegreeTypeRepository extends BaseRepository<DegreeTypeEntity, String> {
}
