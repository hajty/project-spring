package pl.projectmvc.dao;

import org.springframework.data.repository.CrudRepository;
import pl.projectmvc.entity.user.User;

import java.util.List;
import java.util.Optional;

public interface UserDao extends CrudRepository<User, Integer>
{
    public User findByLogin(String login);

    @Override
    public Optional<User> findById(Integer id);

    @Override
    public List<User> findAll();
}
