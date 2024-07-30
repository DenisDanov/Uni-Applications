package bg.duosoft.uniapplicationdemo.services;

import bg.duosoft.uniapplicationdemo.mappers.nomenclatures.AccessLevelMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures.AccessLevelDTO;
import bg.duosoft.uniapplicationdemo.models.entities.nomenclatures.AccessLevelEntity;
import bg.duosoft.uniapplicationdemo.repositories.AccessLevelRepository;
import bg.duosoft.uniapplicationdemo.services.base.BaseServiceImpl;
import bg.duosoft.uniapplicationdemo.services.impl.AccessLevelServiceImpl;
import bg.duosoft.uniapplicationdemo.validators.nomenclatures.AccessLevelValidator;
import bg.duosoft.uniapplicationdemo.validators.config.ValidationError;
import bg.duosoft.uniapplicationdemo.validators.config.ValidationErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccessLevelServiceImplTest {

    @Mock
    private AccessLevelRepository repository;

    @Mock
    private AccessLevelMapper mapper;

    @Mock
    private AccessLevelValidator validator;

    private BaseServiceImpl<String,AccessLevelDTO,AccessLevelEntity,AccessLevelMapper,AccessLevelValidator,AccessLevelRepository> accessLevelService;

    @BeforeEach
    void setUp() {
        accessLevelService = new AccessLevelServiceImpl();
        accessLevelService.setMapper(mapper);
        accessLevelService.setValidator(validator);
        accessLevelService.setRepository(repository);
    }

    @Test
    void testCreate_Success() {
        AccessLevelDTO dto = new AccessLevelDTO();
        AccessLevelEntity entity = new AccessLevelEntity();

        when(validator.validate(dto, true, repository)).thenReturn(new ArrayList<>());
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        AccessLevelDTO result = accessLevelService.create(dto);

        assertNotNull(result);
        verify(validator).validate(dto, true, repository);
        verify(repository).save(entity);
        verify(mapper).toDto(entity);
    }

    @Test
    void testCreate_WithValidationError() {
        AccessLevelDTO dto = new AccessLevelDTO();
        List<ValidationError> errors = List.of(ValidationError.create("field", "error"));

        when(validator.validate(dto, true, repository)).thenReturn(errors);

        assertThrows(ValidationErrorException.class, () -> accessLevelService.create(dto));
    }

    @Test
    void testGetById_Found() {
        String id = "testId";
        AccessLevelEntity entity = new AccessLevelEntity();
        AccessLevelDTO dto = new AccessLevelDTO();

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        AccessLevelDTO result = accessLevelService.getById(id);

        assertNotNull(result);
        assertEquals(dto, result);
    }

    @Test
    void testGetById_NotFound() {
        String id = "testId";

        when(repository.findById(id)).thenReturn(Optional.empty());

        AccessLevelDTO result = accessLevelService.getById(id);

        assertNull(result);
    }

    @Test
    void testUpdate_Success() {
        AccessLevelDTO dto = new AccessLevelDTO();
        AccessLevelEntity entity = new AccessLevelEntity();

        when(validator.validate(dto, false)).thenReturn(new ArrayList<>());
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        AccessLevelDTO result = accessLevelService.update(dto);

        assertNotNull(result);
        verify(validator).validate(dto, false);
        verify(repository).save(entity);
        verify(mapper).toDto(entity);
    }

    @Test
    void testUpdate_WithValidationError() {
        AccessLevelDTO dto = new AccessLevelDTO();
        List<ValidationError> errors = List.of(ValidationError.create("field", "error"));

        when(validator.validate(dto, false)).thenReturn(errors);

        assertThrows(ValidationErrorException.class, () -> accessLevelService.update(dto));
    }

    @Test
    void testDelete() {
        AccessLevelDTO dto = new AccessLevelDTO();
        AccessLevelEntity entity = new AccessLevelEntity();

        when(mapper.toEntity(dto)).thenReturn(entity);

        accessLevelService.delete(dto);

        verify(repository).delete(entity);
    }

    @Test
    void testDeleteById() {
        String id = "testId";

        accessLevelService.deleteById(id);

        verify(repository).deleteById(id);
    }

    @Test
    void testGetAll() {
        AccessLevelEntity entity = new AccessLevelEntity();
        AccessLevelDTO dto = new AccessLevelDTO();
        List<AccessLevelEntity> entities = List.of(entity);
        List<AccessLevelDTO> dtos = List.of(dto);

        when(repository.findAll()).thenReturn(entities);
        when(mapper.toDtoList(entities)).thenReturn(dtos);

        List<AccessLevelDTO> result = accessLevelService.getAll();

        assertNotNull(result);
        assertEquals(dtos, result);
    }
}
