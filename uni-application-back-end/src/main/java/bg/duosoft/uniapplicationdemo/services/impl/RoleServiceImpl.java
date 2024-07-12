package bg.duosoft.uniapplicationdemo.services.impl;

import bg.duosoft.uniapplicationdemo.mappers.nomenclatures.RoleMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures.RoleDTO;
import bg.duosoft.uniapplicationdemo.models.entities.nomenclatures.RoleEntity;
import bg.duosoft.uniapplicationdemo.repositories.RoleRepository;
import bg.duosoft.uniapplicationdemo.services.RoleService;
import bg.duosoft.uniapplicationdemo.services.base.BaseServiceImpl;
import bg.duosoft.uniapplicationdemo.validators.nomenclatures.RoleValidator;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends BaseServiceImpl<String,RoleDTO, RoleEntity, RoleMapper, RoleValidator, RoleRepository> implements RoleService {
}
