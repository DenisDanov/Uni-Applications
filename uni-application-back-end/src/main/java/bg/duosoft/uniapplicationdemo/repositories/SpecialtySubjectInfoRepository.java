package bg.duosoft.uniapplicationdemo.repositories;

import bg.duosoft.uniapplicationdemo.models.entities.SpecialtySubjectInfoEntity;
import bg.duosoft.uniapplicationdemo.models.entities.compositeKeys.SpecialtiesSubjectsId;
import bg.duosoft.uniapplicationdemo.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpecialtySubjectInfoRepository extends BaseRepository<SpecialtySubjectInfoEntity, SpecialtiesSubjectsId> {
    @Query("SELECT ss FROM SpecialtySubjectInfoEntity ss WHERE ss.id.specialtyId = :specialtyId AND ss.id.subjectId = :subjectId")
    Optional<SpecialtySubjectInfoEntity> findBySubjectId(Long specialtyId, Long subjectId);
}
