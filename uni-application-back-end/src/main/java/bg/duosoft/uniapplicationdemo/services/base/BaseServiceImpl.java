package bg.duosoft.uniapplicationdemo.services.base;

import bg.duosoft.uniapplicationdemo.mappers.base.BaseObjectMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.base.BaseDTO;
import bg.duosoft.uniapplicationdemo.repositories.base.BaseRepository;
import bg.duosoft.uniapplicationdemo.validators.config.BadRequestException;
import bg.duosoft.uniapplicationdemo.validators.config.ValidationError;
import bg.duosoft.uniapplicationdemo.validators.config.ValidationErrorException;
import bg.duosoft.uniapplicationdemo.validators.config.Validator;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Getter
public abstract class BaseServiceImpl<ID extends Serializable, DTO extends BaseDTO<ID>, E extends Serializable, M extends BaseObjectMapper<E, DTO>, V extends Validator<DTO>, R extends BaseRepository<E, ID>> implements BaseService<ID, DTO> {

    @Autowired(required = false)
    private R repository;

    @Autowired(required = false)
    private M mapper;

    @Autowired(required = false)
    private V validator;

    @Override
    @Caching(evict = {
            @CacheEvict(cacheResolver = "crudCacheResolver", key = "#root.targetClass.simpleName"),
            @CacheEvict(cacheResolver = "crudCacheResolver", condition = "#dto.id != null", key = "#dto.id")
    })
    public DTO create(DTO dto) {
        if (Objects.isNull(dto)) {
            throw new BadRequestException();
        }
        List<ValidationError> errors = validator.validate(dto, true, repository);
        if (!CollectionUtils.isEmpty(errors)) {
            throw new ValidationErrorException(errors);
        }
        DTO saved = mapper.toDto(repository.save(mapper.toEntity(dto)));
        return saved;
    }

    @Override
    @Cacheable(cacheResolver = "crudCacheResolver", key = "#id")
    public DTO getById(ID id) {
        return mapper.toDto(repository.findById(id).orElse(null));
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheResolver = "crudCacheResolver", key = "#root.targetClass.simpleName"),
            @CacheEvict(cacheResolver = "crudCacheResolver", key = "#dto.id")
    })
    public DTO update(DTO dto) {
        if (Objects.isNull(dto)) {
            throw new BadRequestException();
        }
        List<ValidationError> errors = validator.validate(dto, false);
        if (!CollectionUtils.isEmpty(errors)) {
            throw new ValidationErrorException(errors);
        }
        DTO saved = mapper.toDto(repository.save(mapper.toEntity(dto)));
        return saved;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheResolver = "crudCacheResolver", key = "#root.targetClass.simpleName"),
            @CacheEvict(cacheResolver = "crudCacheResolver", key = "#dto.id")
    })
    public void delete(DTO dto) {
        repository.delete(mapper.toEntity(dto));
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheResolver = "crudCacheResolver", key = "#root.targetClass.simpleName"),
            @CacheEvict(cacheResolver = "crudCacheResolver", key = "#id")
    })
    public void deleteById(ID id) {
        repository.deleteById(id);
    }

    @Override
    @Cacheable(cacheResolver = "crudCacheResolver", key = "#root.targetClass.simpleName")
    public List<DTO> getAll() {
        List<E> entities = repository.findAll();
        return mapper.toDtoList(entities);
    }

    @Override
    public String getCacheName() {
        return this.getClass().getSimpleName();
    }
}
