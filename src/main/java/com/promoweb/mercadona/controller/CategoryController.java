package com.promoweb.mercadona.controller;


import com.promoweb.mercadona.model.Category;
import com.promoweb.mercadona.model.Product;
import com.promoweb.mercadona.model.User;
import com.promoweb.mercadona.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Controller
@RequestMapping("/api/categories")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping("/index")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "4") int size,
                        @RequestParam(name = "keyword", defaultValue = "") String kw) {
        // Utilisez votre méthode de recherche utilisateur avec pagination
        Page<Category> pageCategories = categoryService.findCategoriesWithPagination(kw, PageRequest.of(page, size));

        model.addAttribute("categories", pageCategories.getContent());
        model.addAttribute("pages", new int[pageCategories.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", kw);

        return "/categories/listCategories";
    }
    //Read
    @GetMapping("/editCategory/{id}")
    public String getCategoryById(@PathVariable Long id, Model model) {
        Category category = categoryService.getCategoryById(id);
        if (category != null) {
            model.addAttribute("category", category);
            return "/categories/editCategory";
        } else {
            throw new EntityNotFoundException("La catégorie avec l'id : " + id + " n'existe pas");
        }
    }

    @PostMapping("/editCategory/{id}")
    public String updateCategory(@PathVariable Long id,
                                 @ModelAttribute @Valid Category category,
                                 BindingResult bindingResult,
                                 RedirectAttributes attributes,
                                 Model model) {


        if (bindingResult.hasErrors()) {
            return "/categories/editCategory";
        }
        try {

            model.addAttribute("category", category);
            categoryService.updateCategory(category);
            attributes.addFlashAttribute("message", "Mis à jour de la catégorie avec succès");
            return "redirect:../index";

        } catch (Exception e) {
            logger.warn("Problème est survenu lors de la mise à jour de la catégorie : {} ", e.getMessage());
            attributes.addFlashAttribute("error", "Problème est survenu lors de la mise à jour de la catégorie");
        }
        return "redirect:../index";
    }

    //Create
    @GetMapping("/formCategory")
    public String formCategory(Model model) {
        model.addAttribute("category", new Category());
        return "/categories/formCategory";
    }
    @PostMapping("/formCategory")
    public String createCategory(@Valid @ModelAttribute("category") Category category,
                             BindingResult bindingResult,
                             RedirectAttributes attributes,
                             Model model) {
        if (bindingResult.hasErrors()) {
            return "/categories/formCategory";
        }
        try {
            categoryService.createCategory(category);
            //Ajouter l'utilisateur créer au modèle pour l'affichage sur la page suivante
            //model.addAttribute("createdCategory", createCategory);
            attributes.addFlashAttribute("message", "La catégories a été créé avec succès");
            return "redirect:index";
        } catch (Exception e) {
            logger.warn("Problème est survenu lors de la création de la catégorie : {} ", e.getMessage());
            attributes.addFlashAttribute("error", "L'ajout d'un nouveau catégorie a échoué");
        }
        return "redirect:index";
    }


    //Delete
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id,
                                RedirectAttributes attributes) {
        try {

            Category category = categoryService.getCategoryById(id);

            Boolean status = !category.getStatus();

                category.setStatus(status);

                categoryService.setStatusCategory(category);
                String message = status ? "activée" : "désactivée";

                attributes.addFlashAttribute("message", "La catégorie a été " + message + " avec succès");

        } catch(Exception e){
            logger.warn("Problème est survenu lors de la suppression de l'utilisateur : {} ", e.getMessage());

            attributes.addFlashAttribute("error", "Problème est survenu lors de la désactivation  de la catégorie: " + e.getMessage());
        }


        return "redirect:../index";
    }

    //Show categories
    @GetMapping("/allCategories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
}
