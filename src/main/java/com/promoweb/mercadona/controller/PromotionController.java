package com.promoweb.mercadona.controller;
import com.promoweb.mercadona.model.Product;
import com.promoweb.mercadona.model.Promotion;
import com.promoweb.mercadona.service.ProductService;
import com.promoweb.mercadona.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/api/promotions")
public class PromotionController {

    private final PromotionService promotionService;
    private final ProductService productService;

    @Autowired
    public PromotionController(PromotionService promotionService, ProductService productService) {
        this.promotionService = promotionService;
        this.productService = productService;
    }

    @GetMapping("/index")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "4") int size,
                        @RequestParam(name = "keyword", defaultValue = "") String kw) {
        // Utilisez votre méthode de recherche promotion avec pagination
        Page<Promotion> pagePromotions = promotionService.findPromotionsWithPagination(kw, PageRequest.of(page, size));

        model.addAttribute("promotions", pagePromotions.getContent());
        model.addAttribute("pages", new int[pagePromotions.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", kw);

        return "/listPromotion";
    }

    @GetMapping
    public String getAllPromotions(Model model) {
        List<Promotion> promotions = promotionService.getAllPromotions();
        model.addAttribute("promotions", promotions);
        return "listPromotion";
    }

    @GetMapping("/addPromotionModal")
    public String showAddPromotionForm(Model model) {
        model.addAttribute("promotion", new Promotion());
        return "/products/addPromotion";
    }

    @PostMapping("/add")
    public String addPromotion(@ModelAttribute Promotion promotion) {
        Promotion createdPromotion = promotionService.createPromotion(promotion);
        if (createdPromotion != null) {
            return "redirect:/promotions";
        } else {
            return "redirect:/promotions/add?error";
        }
    }

    @GetMapping("/update/{id}")
    public String showUpdatePromotionForm(@PathVariable Long id, Model model) {
        Promotion promotion = promotionService.getPromotionById(id);
        model.addAttribute("promotion", promotion);
        return "updatePromotion";
    }

    @PostMapping("/update/{id}")
    public String updatePromotion(@PathVariable Long id, @ModelAttribute Promotion promotion) {
        promotionService.updatePromotion(id, promotion);
        return "redirect:/promotions";
    }

    @GetMapping("/delete/{id}")
    public String deletePromotion(@PathVariable Long id) {
        promotionService.deletePromotion(id);
        return "redirect:/promotions";
    }

    // Nouvelle méthode pour calculer le pourcentage en fonction du prix du produit
    /*@GetMapping("/calculatePercentage/{id}/{product_id}")
    @ResponseBody
    public double calculatePercentage(@PathVariable Long id, @PathVariable Long product_id) {
        Promotion promotion = promotionService.getPromotionById(id);
        Product product = productService.getProductById(product_id);
        return promotionService.calculatePercentage(promotion, product);
    }*/
}
