package pl.projectmvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.projectmvc.dao.NewsDao;
import pl.projectmvc.dao.UserDao;
import pl.projectmvc.dao.UserRoleDao;
import pl.projectmvc.entity.News;
import pl.projectmvc.entity.user.User;
import pl.projectmvc.entity.user.UserRole;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class ProjectmvcApplication {

    @Autowired
    UserDao userDao;

    @Autowired
    UserRoleDao userRoleDao;

    @Autowired
    NewsDao newsDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(ProjectmvcApplication.class, args);
    }

    @PostConstruct
    public void init()
    {
        User user = new User("Bartosz", "Miłosierny", "admin", passwordEncoder.encode("passwd"));
        UserRole userRoleAdmin = new UserRole(user, UserRole.ROLE_ADMIN);
        UserRole userRoleUser = new UserRole(user, UserRole.ROLE_USER);

        userDao.save(user);
        userRoleDao.save(userRoleAdmin);
        userRoleDao.save(userRoleUser);

        News news = new News("test", "teksttekstteksttekstteksttekstteksttekstteksttekstteksttekst", user);
        newsDao.save(news);
    }
}
