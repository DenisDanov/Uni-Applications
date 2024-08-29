package bg.duosoft.uniapplicationdemo.repositories;

import bg.duosoft.uniapplicationdemo.models.entities.NewsEntity;
import bg.duosoft.uniapplicationdemo.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends BaseRepository<NewsEntity, Integer> {
}
