package bg.duosoft.uniapplicationdemo.repositories;

import bg.duosoft.uniapplicationdemo.models.entities.FacultyEntity;
import bg.duosoft.uniapplicationdemo.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacultyRepository extends BaseRepository<FacultyEntity, Long> {
    Optional<FacultyEntity> findByFacultyName(String facultyName);

    @Query("SELECT fa FROM FacultyEntity fa JOIN fa.specialties s " +
            "WHERE s.specialtyName = :specialtyName")
    FacultyEntity findBySpecialtyName(String specialtyName);
}
