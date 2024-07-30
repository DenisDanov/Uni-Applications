package bg.duosoft.uniapplicationdemo.services.impl;

import bg.duosoft.uniapplicationdemo.mappers.nomenclatures.AccessLevelMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures.AccessLevelDTO;
import bg.duosoft.uniapplicationdemo.models.entities.nomenclatures.AccessLevelEntity;
import bg.duosoft.uniapplicationdemo.repositories.AccessLevelRepository;
import bg.duosoft.uniapplicationdemo.services.AccessLevelService;
import bg.duosoft.uniapplicationdemo.services.base.BaseServiceImpl;
import bg.duosoft.uniapplicationdemo.validators.nomenclatures.AccessLevelValidator;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class AccessLevelServiceImpl extends BaseServiceImpl<String,AccessLevelDTO,AccessLevelEntity,AccessLevelMapper,AccessLevelValidator,AccessLevelRepository> implements AccessLevelService {
}
