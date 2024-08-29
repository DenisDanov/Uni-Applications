package bg.duosoft.uniapplicationdemo.services;

import bg.duosoft.uniapplicationdemo.models.dtos.NewsDTO;
import bg.duosoft.uniapplicationdemo.services.base.BaseService;

public interface NewsService extends BaseService<Integer, NewsDTO> {

    @Override
    default boolean isCachingEnabled() {
        return false;
    }
}
