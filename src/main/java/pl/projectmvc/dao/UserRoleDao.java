package pl.projectmvc.dao;

import org.springframework.data.repository.CrudRepository;
import pl.projectmvc.entity.user.UserRole;

public interface UserRoleDao extends CrudRepository<UserRole, Integer>
{
}
