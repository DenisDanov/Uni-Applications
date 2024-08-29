package bg.duosoft.uniapplicationdemo.services.impl;

import bg.duosoft.uniapplicationdemo.mappers.NewsMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.NewsDTO;
import bg.duosoft.uniapplicationdemo.models.entities.NewsEntity;
import bg.duosoft.uniapplicationdemo.repositories.NewsRepository;
import bg.duosoft.uniapplicationdemo.services.NewsService;
import bg.duosoft.uniapplicationdemo.services.base.BaseServiceImpl;
import bg.duosoft.uniapplicationdemo.validators.NewsValidator;
import org.springframework.stereotype.Service;

@Service
public class NewsServiceImpl extends BaseServiceImpl<Integer, NewsDTO, NewsEntity, NewsMapper, NewsValidator, NewsRepository> implements NewsService {
}
