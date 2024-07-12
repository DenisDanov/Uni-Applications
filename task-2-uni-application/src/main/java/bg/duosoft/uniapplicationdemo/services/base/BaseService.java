package bg.duosoft.uniapplicationdemo.services.base;

import java.io.Serializable;
import java.util.List;

public interface BaseService <ID extends Serializable, DTO extends Serializable> {
    DTO create(DTO dto);

    DTO getById(ID id);

    DTO update(DTO dto);

    void delete(DTO dto) throws Exception;

    void deleteById(ID id) throws Exception;

    List<DTO> getAll();

    String getCacheName();

    default boolean isCachingEnabled() {
        return true;
    }
}

