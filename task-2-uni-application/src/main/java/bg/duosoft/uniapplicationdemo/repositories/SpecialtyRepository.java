package bg.duosoft.uniapplicationdemo.repositories;

import bg.duosoft.uniapplicationdemo.models.entities.SpecialtyEntity;
import bg.duosoft.uniapplicationdemo.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecialtyRepository extends BaseRepository<SpecialtyEntity, Long> {
    Optional<SpecialtyEntity> findBySpecialtyName(String name);

    List<SpecialtyEntity> findAllByFacultyId(Long id);
}
