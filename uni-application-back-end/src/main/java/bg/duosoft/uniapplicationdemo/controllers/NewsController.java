package bg.duosoft.uniapplicationdemo.controllers;

import bg.duosoft.uniapplicationdemo.controllers.base.CrudController;
import bg.duosoft.uniapplicationdemo.models.dtos.NewsDTO;
import bg.duosoft.uniapplicationdemo.services.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class NewsController extends CrudController<Integer, NewsDTO, NewsService> {

    @Override
    public List<NewsDTO> getAll() {
        return super.getService().getAll();
    }
}
