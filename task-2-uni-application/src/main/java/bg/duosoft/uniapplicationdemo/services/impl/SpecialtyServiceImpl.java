package bg.duosoft.uniapplicationdemo.services.impl;

import bg.duosoft.uniapplicationdemo.mappers.SpecialtyMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.SpecialtyDTO;
import bg.duosoft.uniapplicationdemo.models.entities.SpecialtyEntity;
import bg.duosoft.uniapplicationdemo.repositories.SpecialtyRepository;
import bg.duosoft.uniapplicationdemo.services.SpecialtyService;
import bg.duosoft.uniapplicationdemo.services.base.BaseServiceImpl;
import bg.duosoft.uniapplicationdemo.validators.SpecialtyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SpecialtyServiceImpl extends BaseServiceImpl<Long, SpecialtyDTO, SpecialtyEntity, SpecialtyMapper, SpecialtyValidator, SpecialtyRepository> implements SpecialtyService {

    private final CacheManager cacheManager;

    @Override
    public Optional<SpecialtyDTO> findBySpecialtyName(String name) {
        return super.getRepository().findBySpecialtyName(name).map(specialtyEntity -> super.getMapper().toDto(specialtyEntity));
    }

    @Override
    @Cacheable(cacheNames = "faculty", key = "#facultyId")
    public List<SpecialtyDTO> findByFacultyId(Long facultyId) {
        if (cacheManager.getCache(this.getClass().getSimpleName()).get(this.getClass().getSimpleName()) == null) {
            return super.getMapper().toDtoList(super.getRepository().findAllByFacultyId(facultyId));
        } else {
            List<SpecialtyDTO> specialtyDTOList = (List<SpecialtyDTO>) cacheManager.getCache(this.getClass().getSimpleName()).get(this.getClass().getSimpleName()).get();
            return specialtyDTOList.stream().filter(specialtyDTO -> Objects.equals(specialtyDTO.getFacultyID(), facultyId)).findAny().stream().toList();
        }
    }
}
