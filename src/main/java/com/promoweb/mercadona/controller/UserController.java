package com.promoweb.mercadona.controller;


import com.promoweb.mercadona.model.User;
import com.promoweb.mercadona.service.UserService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
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
        // Utilisez votre méthode de recherche utilisateur avec pagination
        Page<User> pageUsers = userService.findUsersWithPagination(kw, PageRequest.of(page, size));

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
            //ResponseEntity.ok(user);
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
            userService.createUser(user);
            attributes.addFlashAttribute("message", "L'utilisateur a été créé avec succès");

            return "redirect:index";

        } catch (Exception e) {
            logger.warn("Problème est survenu lors de la création de l'utilisateur : {} ", e.getMessage());
            attributes.addFlashAttribute("error", "L'ajout d'un nouveau utilisateur a échoué");
        }
        return "redirect:index";
    }

    // Liste des  utulisateurs
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

            model.addAttribute("user", user);
            userService.updateUser(id, user);
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
            userService.deleteUser(id);
            attributes.addFlashAttribute("message", "L'utilisateur a été supprimé avec succès");
            return "redirect:../index";
        } catch (Exception e) {
            logger.warn("Problème est survenu lors de la suppression de l'utilisateur : {} ", e.getMessage());
            attributes.addFlashAttribute("error", "Problème est survenu lors de la suppression de l'utilisateur");
        }
        return "redirect:../index";
    }




}