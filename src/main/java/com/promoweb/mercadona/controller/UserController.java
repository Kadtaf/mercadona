package com.promoweb.mercadona.controller;


import com.promoweb.mercadona.model.User;
import com.promoweb.mercadona.service.UserService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Controller
@RequestMapping("/api/users")
public class UserController {
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

        return "user/listUser";
    }

    @GetMapping("/")
    public String home(){
        return "redirect:user/index";
    }

    //Read
    @GetMapping("/{id}")
    public String getUserById(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        if (user != null) {
            model.addAttribute("user", user);
            return "/profile";
            //ResponseEntity.ok(user);
        } else {
            throw new EntityNotFoundException("L'utilisateur avec l'id : " +id + " n'existe pas");
        }

    }

    //Create

    @GetMapping("/formUser")
    public String formUser(Model model) {
        model.addAttribute("user", new User());
        return "/user/formUser";
    }

    @PostMapping("/saveUser")
    public String createUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) return "user/formUser";

        User createUser = userService.createUser(user);
       //Ajouter l'utilisateur créer au modèle pour l'affichage sur la page suivante
        model.addAttribute("createdUser", createUser);
        return "redirect:/index";
    }

    // Liste des  utulisateurs
    @GetMapping("/listUser")
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "/user/listUser";
    }

    //Update
    @PutMapping("/updateUser/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute("user") User user, Model model) {
        User updatedUser = userService.updateUser(id, user);
        if (updatedUser != null) {
            // On peut ajouter des attributs au modèle si nécessaire
            model.addAttribute("updatedUser", updatedUser);
        } else {
            throw new EntityNotFoundException("L'administrateur avec l'id : " + id + " n'existe pas");
        }
        // Redirige vers la page contenant la liste de tous les utilisateurs
        return "redirect:/user/updateUser";
    }


    //Delete
    @DeleteMapping("/delete")
    public String deleteUser(Long id, String keyword, int page, Model model) {
        userService.deleteUser(id);
        // On peut ajouter des attributs au modèle si nécessaire
        model.addAttribute("message", "L'administrateur a été supprimé avec succès.");
        // Redirige vers la page contenant la liste de tous les utilisateurs
        return "redirect:/index?page="+page+"&keyword="+keyword;
    }


}