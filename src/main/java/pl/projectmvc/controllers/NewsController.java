package pl.projectmvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.projectmvc.dao.NewsDao;
import pl.projectmvc.dao.UserDao;
import pl.projectmvc.entity.News;
import pl.projectmvc.entity.user.User;

import java.util.List;
import java.util.Optional;

@Controller
public class NewsController
{
    @Autowired
    NewsDao newsDao;

    @Autowired
    UserDao userDao;

    @GetMapping(value = "/news")
    public String newsPage(@ModelAttribute Object attribute, Model model)
    {
        List<News> news = newsDao.findAll();

        model.addAttribute("news", news);
        model.addAttribute(attribute);

        return "news";
    }

    @GetMapping(value = "/news/id/{id}")
    public String newPage(@PathVariable("id")Integer id, Model model)
    {
        Optional<News> news = newsDao.findById(id);

        if (news.isPresent())
        {
            User user = news.get().getUser();

            model.addAttribute("news", news.get());
            model.addAttribute("author", user.getLogin());
        }

        return "new";
    }

    @GetMapping(value = "/news/add")
    public String addNews(@ModelAttribute Object attribute, Model model)
    {
        model.addAttribute("news", new News());
        model.addAttribute("author_name");
        model.addAttribute("news_status", "added");

        return "new_add";
    }

    @PostMapping(value = "/news/add")
    public String addNews(@ModelAttribute(value = "news")News news, @ModelAttribute(value = "author_name")String authorName, RedirectAttributes attributes)
    {
        news.setUser(userDao.findByLogin(authorName));
        newsDao.save(news);

        attributes.addFlashAttribute("news_status", "added");
        return "redirect:/news";
    }

    @GetMapping(value = "/news/edit/id/{id}")
    public String updateNews(@PathVariable("id")Integer id, Model model)
    {
        Optional<News> news = newsDao.findById(id);

        if (news.isPresent())
        {
            model.addAttribute("news", news.get());

            return "new_edit";
        }
        else return "redirect:/news";
    }

    @PostMapping(value = "/news/edit/id/{id}")
    public String updateNews(@PathVariable(value = "id")Integer id, @ModelAttribute(value = "news")News newNews, RedirectAttributes attributes)
    {
        Optional<News> news = newsDao.findById(id);

        if (news.isPresent())
        {
            news.get().setTitle(newNews.getTitle());
            news.get().setText(newNews.getText());

            newsDao.save(news.get());

            attributes.addFlashAttribute("news_update", "updated");
        }

        return "redirect:/news";
    }

    @GetMapping(value = "/news/delete/id/{id}")
    public String deleteNews(@PathVariable("id")Integer id, RedirectAttributes attributes)
    {
        Optional<News> news = newsDao.findById(id);

        if (news.isPresent())
        {
            newsDao.delete(news.get());
            attributes.addFlashAttribute("news_delete", "deleted");
        }
        else attributes.addFlashAttribute("news_delete", "error");

        return "redirect:/news";
    }
}
