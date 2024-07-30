package bg.duosoft.uniapplicationdemo.services;

import bg.duosoft.uniapplicationdemo.mappers.SpecialtyMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.SpecialtyDTO;
import bg.duosoft.uniapplicationdemo.models.entities.FacultyEntity;
import bg.duosoft.uniapplicationdemo.models.entities.SpecialtyEntity;
import bg.duosoft.uniapplicationdemo.repositories.SpecialtyRepository;
import bg.duosoft.uniapplicationdemo.services.impl.SpecialtyServiceImpl;
import bg.duosoft.uniapplicationdemo.validators.SpecialtyValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpecialtyServiceImplTest {

    @Mock
    private SpecialtyRepository repository;

    @Mock
    private SpecialtyMapper mapper;

    @Mock
    private SpecialtyValidator validator;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    private SpecialtyServiceImpl specialtyService;

    @BeforeEach
    void setUp() {
        specialtyService = new SpecialtyServiceImpl(cacheManager);
        specialtyService.setRepository(repository);
        specialtyService.setMapper(mapper);
        specialtyService.setValidator(validator);
    }

    @Test
    void testFindBySpecialtyNameWhenPresent() {
        SpecialtyEntity entity = new SpecialtyEntity();
        SpecialtyDTO dto = new SpecialtyDTO();
        String specialtyName = "Computer Science";

        when(repository.findBySpecialtyName(specialtyName)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        Optional<SpecialtyDTO> result = specialtyService.findBySpecialtyName(specialtyName);

        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
    }

    @Test
    void testFindBySpecialtyNameWhenNotPresent() {
        String specialtyName = "Computer Science";

        when(repository.findBySpecialtyName(specialtyName)).thenReturn(Optional.empty());

        Optional<SpecialtyDTO> result = specialtyService.findBySpecialtyName(specialtyName);

        assertFalse(result.isPresent());
    }

    @Test
    void testFindByFacultyIdCacheHit() {
        Long facultyId = 1L;
        SpecialtyDTO dto = new SpecialtyDTO();
        dto.setFacultyID(facultyId);
        List<SpecialtyDTO> dtos = Collections.singletonList(dto);

        // Create a mock Cache.ValueWrapper to wrap the list of SpecialtyDTO
        Cache.ValueWrapper valueWrapper = Mockito.mock(Cache.ValueWrapper.class);
        when(valueWrapper.get()).thenReturn(dtos);

        // Configure the cache mock to return the ValueWrapper
        when(cache.get(anyString())).thenReturn(valueWrapper);

        when(cacheManager.getCache("SpecialtyServiceImpl")).thenReturn(cache);

        List<SpecialtyDTO> result = specialtyService.findByFacultyId(facultyId);

        assertEquals(dtos, result);
    }

    @Test
    void testFindByFacultyIdCacheMiss() {
        Long facultyId = 2L;
        SpecialtyEntity entity = new SpecialtyEntity();
        entity.setFaculty(new FacultyEntity());
        SpecialtyDTO dto = new SpecialtyDTO();
        dto.setFacultyID(facultyId);
        List<SpecialtyEntity> entities = Collections.singletonList(entity);
        List<SpecialtyDTO> dtos = Collections.singletonList(dto);

        // Simulate cache miss
        when(cache.get(anyString())).thenReturn(null);
        when(cacheManager.getCache("SpecialtyServiceImpl")).thenReturn(cache);

        // Mock repository and mapper
        when(repository.findAllByFacultyId(facultyId)).thenReturn(entities);
        when(mapper.toDtoList(entities)).thenReturn(dtos);

        List<SpecialtyDTO> result = specialtyService.findByFacultyId(facultyId);

        assertEquals(dtos, result);
    }
}
