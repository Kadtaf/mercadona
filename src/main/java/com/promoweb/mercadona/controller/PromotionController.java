package com.promoweb.mercadona.controller;

import com.promoweb.mercadona.model.Product;
import com.promoweb.mercadona.model.Promotion;
import com.promoweb.mercadona.service.ProductService;
import com.promoweb.mercadona.service.PromotionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("/api/promotions")
public class PromotionController {

    private final ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final PromotionService promotionService;

    @Autowired
    public PromotionController(PromotionService promotionService, ProductService productService, ProductService productService1) {
        this.promotionService = promotionService;
        this.productService = productService1;
    }

    @GetMapping("/index")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "10") int size,
                        @RequestParam(name = "keyword", defaultValue = "") String kw) {

        // Utilisez votre méthode de recherche promotion avec pagination
        Page<Promotion> pagePromotions = promotionService.findPromotionsWithPagination(kw, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));

        model.addAttribute("promotions", pagePromotions.getContent());
        model.addAttribute("pages", new int[pagePromotions.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", kw);

        return "/promotions/listPromotion";
    }

    @GetMapping
    public String getAllPromotions(Model model) {
        List<Promotion> promotions = promotionService.getAllPromotions();
        model.addAttribute("promotions", promotions);
        return "listPromotion";
    }

    //Create
    @GetMapping("/formPromotion")
    public String formPromotion(Model model) {
        model.addAttribute("promotion", new Promotion());
        return "/promotions/formPromotion";
    }
    @PostMapping("/formPromotion")
    public String createPromotion(@Valid @ModelAttribute("promotion") Promotion promotion,
                                 BindingResult bindingResult,
                                 RedirectAttributes attributes,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            return "/promotions/formPromotion";
        }
        try {
            promotionService.createPromotion(promotion);
            attributes.addFlashAttribute("message", "La promotion a été créé avec succès");
            return "redirect:index";
        } catch (Exception e) {
            logger.warn("Problème est survenu lors de la création de la promotion : {} ", e.getMessage());
            attributes.addFlashAttribute("error", "L'ajout d'une nouvelle promotion a échoué");
        }
        return "redirect:index";
    }

    @GetMapping("/editPromotion/{id}")
    public String showPromotionForm(@PathVariable Long id, Model model) {
        Promotion promotion = promotionService.getPromotionById(id);
        if (promotion != null) {
            model.addAttribute("promotion", promotion);
            return "/promotions/editPromotion";
        } else {
            throw new EntityNotFoundException("La catégorie avec l'id : " + id + " n'existe pas");
        }
    }

    @PostMapping("/editPromotion/{id}")
    public String updatePromotion(@PathVariable Long id,
                                 @ModelAttribute @Valid Promotion promotion,
                                 BindingResult bindingResult,
                                 RedirectAttributes attributes,
                                 Model model) {

        if (bindingResult.hasErrors()) {
            return "/promotions/editPromotion";
        }
        try {

            model.addAttribute("promotion", promotion);
            promotionService.updatePromotion(promotion);
            attributes.addFlashAttribute("message", "Mis à jour de la promotion avec succès");
            return "redirect:../index";

        } catch (Exception e) {
            logger.warn("Problème est survenu lors de la mise à jour de la promotion : {} ", e.getMessage());
            attributes.addFlashAttribute("error", "Problème est survenu lors de la mise à jour de la promotion");
        }
        return "redirect:../index";
    }

    @GetMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> deletePromotion(@PathVariable Long id) {
        try {

            promotionService.deletePromotion(id);
            return ResponseEntity.ok("La promotion a été supprimée avec succès");
        } catch (Exception e) {
            logger.warn("Problème est survenu lors de la suppression de la promotion : {} ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la suppression de la promotion :"+ e.getMessage());
        }
    }

}
