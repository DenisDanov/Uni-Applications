package bg.duosoft.uniapplicationdemo.repositories;

import bg.duosoft.uniapplicationdemo.models.entities.SubjectEntity;
import bg.duosoft.uniapplicationdemo.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectRepository extends BaseRepository<SubjectEntity, Long> {
    Optional<SubjectEntity> findBySubjectName(String name);
}
