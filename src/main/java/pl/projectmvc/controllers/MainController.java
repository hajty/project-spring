package pl.projectmvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.projectmvc.dao.UserDao;
import pl.projectmvc.dao.UserRoleDao;
import pl.projectmvc.entity.user.User;
import pl.projectmvc.entity.user.UserRole;

@Controller
public class MainController
{
    @Autowired
    UserDao userDao;

    @Autowired
    UserRoleDao userRoleDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home()
    {
        return "index";
    }

    @RequestMapping(value = "/accessDenied", method = RequestMethod.GET)
    public String accessDenied()
    {
        return "access_denied";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage()
    {
        return "login";
    }

    @RequestMapping(value = "/login-error", method = RequestMethod.GET)
    public String loginError(Model model)
    {
        model.addAttribute("login_error", true);
        return "login.html";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerPage(@ModelAttribute Object attribute, Model model)
    {
        model.addAttribute("user", new User());
        model.addAttribute(attribute);

        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@ModelAttribute(value = "user")User user, RedirectAttributes attributes)
    {
        User fromDatabase = userDao.findByLogin(user.getLogin());

        if (fromDatabase == null)
        {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userDao.save(user);
            userRoleDao.save(new UserRole(user, UserRole.ROLE_USER));

            attributes.addFlashAttribute("registered", true);
        }
        else attributes.addFlashAttribute("registered", "user_exists");

        return "redirect:/register";
    }
}