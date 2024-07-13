package bg.duosoft.uniapplicationdemo.controllers.base;

import bg.duosoft.uniapplicationdemo.exceptions.ResourceNotFoundException;
import bg.duosoft.uniapplicationdemo.services.base.BaseService;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Getter
public abstract class CrudController<ID extends Serializable, DTO extends Serializable, S extends BaseService<ID, DTO>> extends BaseAccessController {

    @Autowired(required = false)
    private S service;

    private static final Logger logger = LoggerFactory.getLogger(CrudController.class);

    @GetMapping(value = "/{id}")
    public DTO getById(@PathVariable("id") ID id) {
        if (isProtected()) {
            checkPermissions(getAdminRole());
            checkPermissions(getReadOnlyRole() + "-or-" + getAccessRole());
        }
        DTO result = service.getById(id);
        if (Objects.isNull(result)) {
            throw new ResourceNotFoundException("Not found");
        }
        return result;
    }

    @GetMapping
    public List<DTO> getAll() {
        if (isProtected()) {
            checkPermissions(getReadOnlyRole() + "-or-" + getAccessRole());
        }
        return service.getAll();
    }

    @PostMapping
    public DTO create(@RequestBody DTO dto) {
        if (isProtected()) {
            checkPermissions(getAccessRole() + "-and-" + getAdminRole());
        }
        return service.create(dto);
    }

    @PutMapping
    public DTO update(@RequestBody DTO dto) {
        if (isProtected()) {
            checkPermissions(getAccessRole()+ "-and-" + getAdminRole());
        }
        return service.update(dto);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") ID id) throws Exception {
        if (isProtected()) {
            checkPermissions(getAccessRole() + "-and-" + getAdminRole());
        }
        try {
            service.deleteById(id);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
