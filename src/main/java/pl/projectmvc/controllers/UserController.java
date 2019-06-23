package pl.projectmvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.projectmvc.dao.UserDao;
import pl.projectmvc.entity.user.User;

import java.io.Console;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController
{
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserDao userDao;

    @GetMapping(value = "/panel/user")
    public String panelUser()
    {
        return "panel_user";
    }

    @GetMapping(value = "/panel/user/changepassword")
    public String changePassword(@ModelAttribute Object attribute, Model model)
    {
        model.addAttribute("new_password");
        model.addAttribute(attribute);

        return "changepassword";
    }

    @GetMapping(value = "/panel/admin")
    public String panelAdmin()
    {
        return "panel_admin";
    }

    @GetMapping(value = "/panel/admin/manageusers")
    public String manageUsers(Model model)
    {
        List<User> users = userDao.findAll();

        model.addAttribute("users", users);

        return "manage_users";
    }

    @GetMapping(value = "/panel/admin/manageusers/delete/{id}")
    public String deleteUser(@PathVariable("id")Integer id, RedirectAttributes attributes)
    {
        Optional<User> user = userDao.findById(id);

        if (user.isPresent())
        {
            userDao.delete(user.get());
            attributes.addFlashAttribute("user_delete", "deleted");
        }
        else attributes.addFlashAttribute("user_delete", "error");

        return "redirect:/panel/admin/manageusers";
    }

    @PostMapping(value = "/panel/user/changepassword")
    public String changePassword(@ModelAttribute(value = "new_password")String newPassword,
                             @ModelAttribute(value = "user_name")String userName,
                             RedirectAttributes attributes)
    {
        User user = userDao.findByLogin(userName);

        user.setPassword(passwordEncoder.encode(newPassword));
        userDao.save(user);
        attributes.addFlashAttribute("password_changed", true);

        return "redirect:/panel/user/changepassword";
    }
}
