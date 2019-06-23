package pl.projectmvc.dao;

import org.springframework.data.repository.CrudRepository;
import pl.projectmvc.entity.News;

import java.util.List;
import java.util.Optional;

public interface NewsDao extends CrudRepository<News, Integer>
{
    @Override
    public Optional<News> findById(Integer id);

    @Override
    public List<News> findAll();


}
