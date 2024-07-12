package bg.duosoft.uniapplicationdemo.repositories;

import bg.duosoft.uniapplicationdemo.models.entities.DeclinedStudentApplicationEntity;
import bg.duosoft.uniapplicationdemo.models.entities.compositeKeys.DeclinedStudentApplicationId;
import bg.duosoft.uniapplicationdemo.repositories.base.BaseRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeclinedStudentApplicationRepository extends BaseRepository<DeclinedStudentApplicationEntity, DeclinedStudentApplicationId> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO declined_student_applications (username, specialty_id, faculty_id, delete_date) VALUES (:#{#entity.id.username}, :#{#entity.id.specialtyId}, :#{#entity.id.facultyId}, :#{#entity.deleteDate})", nativeQuery = true)
    void saveEntity(@Param("entity") DeclinedStudentApplicationEntity entity);

}
