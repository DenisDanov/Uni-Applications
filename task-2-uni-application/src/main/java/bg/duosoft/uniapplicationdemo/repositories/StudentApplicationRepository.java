package bg.duosoft.uniapplicationdemo.repositories;

import bg.duosoft.uniapplicationdemo.models.entities.StudentApplicationEntity;
import bg.duosoft.uniapplicationdemo.models.entities.compositeKeys.StudentsApplicationsId;
import bg.duosoft.uniapplicationdemo.repositories.base.BaseRepository;
import bg.duosoft.uniapplicationdemo.repositories.custom.StudentApplicationRepositoryCustom;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
public interface StudentApplicationRepository extends BaseRepository<StudentApplicationEntity, StudentsApplicationsId>, StudentApplicationRepositoryCustom {
    @Query("SELECT sa FROM StudentApplicationEntity sa WHERE sa.username = :username AND sa.faculty.id = :facultyId AND sa.specialty.id = :specialtyId")
    Optional<StudentApplicationEntity> findByStudentsApplicationsId(String username, Long facultyId, Long specialtyId);

    @Query("SELECT sa FROM StudentApplicationEntity sa WHERE sa.username = :username AND sa.faculty.facultyName = :facultyName AND sa.specialty.specialtyName = :specialtyName")
    Optional<StudentApplicationEntity> findByStudentsApplicationsId(String username, String facultyName, String specialtyName);

    List<StudentApplicationEntity> findStudentApplicationEntitiesByUsername(String username);

    List<StudentApplicationEntity> findStudentApplicationEntitiesByUsernameAndFacultyFacultyName(String username, String facultyName);

    List<StudentApplicationEntity> findStudentApplicationEntitiesByFacultyFacultyName(String facultyName);

    List<StudentApplicationEntity> findStudentApplicationEntitiesByFacultyFacultyNameAndSpecialtySpecialtyName(String facultyName, String specialtyName);

    List<StudentApplicationEntity> findStudentApplicationEntitiesBySpecialtySpecialtyName(String facultyName);

    @Query("SELECT sa FROM StudentApplicationEntity sa WHERE sa.username = :username " +
            "AND sa.faculty.facultyName = :facultyName " +
            "AND sa.specialty.specialtyName = :specialtyName " +
            "AND sa.applicationStatus.applicationStatus = 'PENDING'")
    Optional<StudentApplicationEntity> findByStudentsApplicationsIdAndStatus(String username, String facultyName, String specialtyName);

    @Query("SELECT sa FROM StudentApplicationEntity sa " +
            "WHERE sa.specialty.specialtyName = :specialtyName AND sa.username = :username AND sa.applicationStatus.applicationStatus = 'ACCEPTED'")
    Optional<StudentApplicationEntity> findByStudentsApplicationsIdAndStatusAccepted(String specialtyName, String username);

    @Query("SELECT sa FROM StudentApplicationEntity sa " +
            "WHERE sa.applicationStatus.applicationStatus = 'DECLINED'")
    List<StudentApplicationEntity> findDeclinedApplications();

    @Modifying
    @Query("DELETE FROM StudentApplicationEntity sa WHERE sa.username = :username")
    int deleteByUsername(String username);

    @Modifying
    @Query("DELETE FROM StudentApplicationEntity sa WHERE sa.username = :username AND " +
            "sa.faculty.facultyName = :facultyName AND " +
            "sa.specialty.specialtyName = :specialtyName")
    int deleteByUsernameAndAndFacultyAndSpecialty(String username, String facultyName, String specialtyName);
}
