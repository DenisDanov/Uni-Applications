package bg.duosoft.uniapplicationdemo.mappers;

import bg.duosoft.uniapplicationdemo.mappers.base.BaseObjectMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.NewsDTO;
import bg.duosoft.uniapplicationdemo.models.entities.NewsEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class NewsMapper extends BaseObjectMapper<NewsEntity, NewsDTO> {
}
