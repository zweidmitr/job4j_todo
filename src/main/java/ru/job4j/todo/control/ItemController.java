package ru.job4j.todo.control;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.ItemService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@ThreadSafe
@Controller
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/items")
    public String items(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("items", itemService.findAll());
        return "items";
    }

    @PostMapping("/saveItem")
    public String saveItem(@ModelAttribute Item item, HttpSession session) {
        User user = (User) session.getAttribute("user");
        item.setCreated(LocalDateTime.now().withNano(0));
        item.setUser(user);
        itemService.add(item);
        return "redirect:/items";
    }

    @GetMapping("/addItem")
    public String addItem(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "addItem";
    }

    @GetMapping("/formUpdateItem/{itemId}")
    public String formUpdateItem(Model model, @PathVariable("itemId") int id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user");
        model.addAttribute("item", itemService.findById(id));
        return "updateItem";
    }

    @PostMapping("/updateItem")
    public String updateItem(@ModelAttribute Item item) {
        item.setCreated(LocalDateTime.now().withNano(0));
        itemService.update(item);
        return "redirect:/items";
    }

    @GetMapping("/deleteItem/{itemId}")
    public String deleteItem(@PathVariable("itemId") int id) {
        itemService.delete(id);
        return "redirect:/items";
    }

    @GetMapping("/setDone/{itemId}")
    public String setDone(@PathVariable("itemId") int id) {
        itemService.setDone(id);
        return "redirect:/items";
    }

    @GetMapping("/newItems")
    public String newItems(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("items", itemService.findByDone(false));
        return "items";
    }

    @GetMapping("/doneItems")
    public String doneItems(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("items", itemService.findByDone(true));
        return "items";
    }

    @GetMapping("/descriptionItem/{itemId}")
    public String descriptionItem(Model model, @PathVariable("itemId") int id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("item", itemService.findById(id));
        return "descriptionItem";
    }
}
