package bg.duosoft.uniapplicationdemo.repositories.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseRepository<T extends Serializable, ID> extends JpaRepository<T, ID> {
}