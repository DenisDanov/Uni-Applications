package bg.duosoft.uniapplicationdemo.repositories;

import bg.duosoft.uniapplicationdemo.models.entities.nomenclatures.RoleEntity;
import bg.duosoft.uniapplicationdemo.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends BaseRepository<RoleEntity, String> {
}
