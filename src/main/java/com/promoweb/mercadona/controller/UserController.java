package com.promoweb.mercadona.controller;


import com.promoweb.mercadona.model.User;
import com.promoweb.mercadona.service.UserService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Controller
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final UserService userService;
    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/index")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "4") int size,
                        @RequestParam(name = "keyword", defaultValue = "") String kw) {

        Page<User> pageUsers = userService.findUsersWithPagination(kw, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));

        model.addAttribute("users", pageUsers.getContent());
        model.addAttribute("pages", new int[pageUsers.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", kw);

        return "/users/listUser";
    }

    @GetMapping("/")
    public String home(){
        return "redirect:/index";
    }

    //Read
    @GetMapping("/{id}")
    public String getUserById(@PathVariable Long id, Model model) throws EntityNotFoundException {
        User user = userService.getUserById(id);
        if (user != null) {
            model.addAttribute("user", user);
            return "/users/editUser";

        } else {
            throw new EntityNotFoundException("L'utilisateur avec l'id : " +id + " n'existe pas");
        }
    }

    //Create

    @GetMapping("/formUser")
    public String formUser(Model model) {
        model.addAttribute("user", new User());
        return "/users/formUser";
    }

    @PostMapping("/saveUser")
    public String createUser(@Valid @ModelAttribute("user") User user,
                             BindingResult bindingResult,
                             RedirectAttributes attributes,
                             Model model) {
        if (bindingResult.hasErrors()) {
            return "/users/formUser";
        }
        try {
            User createUser = userService.createUser(user);
            model.addAttribute("createdUser", createUser);
            attributes.addFlashAttribute("message", "L'utilisateur a été créé avec succès");

            return "redirect:index";

        } catch (Exception e) {
            logger.warn("Problème est survenu lors de la création de l'utilisateur : {} ", e.getMessage());
            attributes.addFlashAttribute("error", "L'ajout d'un nouveau utilisateur a échoué");
        }
        return "redirect:index";
    }


    @GetMapping("/listUser")
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "/users/listUser";
    }

    //Update
    @GetMapping( "/editUser/{id}")
    public String editUser(@PathVariable Long id, @ModelAttribute("user") User user, Model model) throws EntityNotFoundException {
        User newuser = userService.getUserById(id);
        if (newuser != null) {
            model.addAttribute("user", newuser);

        } else {
            throw new EntityNotFoundException("L'administrateur avec l'id : " + id + " n'existe pas");
        }

        return "/users/editUser";
    }

    @PostMapping("/updateUser/{id}")
    public String updateUser(@PathVariable Long id,
                             @ModelAttribute @Valid User user,
                             BindingResult bindingResult,
                             RedirectAttributes attributes,
                             Model model) {
        if (bindingResult.hasErrors()) {
            return "/users/editUser" ;
        }
        try {

            userService.updateUser(user);

            attributes.addFlashAttribute("message", "Mis à jour de l'administrateur' avec succès");

            return "redirect:../index";

        } catch (Exception e) {
            logger.warn("Problème est survenu lors de la mise à jour de l'administrateur : {} ", e.getMessage());
            attributes.addFlashAttribute("error", "Problème est survenu lors de la mise à jour de l'administrateur");
        }
        logger.info("Redirection vers /index");
        return "redirect:../index";

    }

    //Delete
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id,
                             RedirectAttributes attributes) {
        try {
            User user = userService.getUserById(id);

            Boolean status = !user.getStatus();
            user.setStatus(status);
            userService.setStatusUser(user);

            String message = status ? "activée" : "désactivée";
            attributes.addFlashAttribute("message", "L'utilisateur a été " + message + " avec succès");

        } catch (Exception e) {
            logger.warn("Problème est survenu lors de la suppression de l'utilisateur : {} ", e.getMessage());
            attributes.addFlashAttribute("error", "Problème est survenu lors de la suppression de l'utilisateur "  + e.getMessage());
        }
        return "redirect:../index";
    }

}