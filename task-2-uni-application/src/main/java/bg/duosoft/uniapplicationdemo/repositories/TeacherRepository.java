package bg.duosoft.uniapplicationdemo.repositories;

import bg.duosoft.uniapplicationdemo.models.entities.TeacherEntity;
import bg.duosoft.uniapplicationdemo.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends BaseRepository<TeacherEntity, Long> {
}
