package com.promoweb.mercadona.controller;
import com.promoweb.mercadona.model.Promotion;
import com.promoweb.mercadona.service.PromotionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping("/promotions")
public class PromotionController {

    private final PromotionService promotionService;

    @Autowired
    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
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

        return "/produits/listPromotion";
    }


    @GetMapping("/add")
    public String showAddPromotionForm(Model model) {
        model.addAttribute("promotion", new Promotion());
        return "/produits/addPromotion";
    }

    @PostMapping("/savePromo")
    public String addPromotion(@Valid @ModelAttribute("promotion") Promotion promotion, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "/produits/addPromotion";
        }
        Promotion createdPromotion = promotionService.addPromotion(promotion);
        model.addAttribute("createdPromotion", createdPromotion);
        if (createdPromotion != null) {
            return "redirect:/listPromotion";
        } else {
            return "redirect:/listPromotion/add?error";
        }
    }

    @GetMapping("/update/{id}")
    public String showUpdatePromotionForm(@PathVariable Long id, Model model) {
        Promotion promotion = promotionService.getPromotionById(id);
        model.addAttribute("promotion", promotion);
        return "/produits/updatePromotion";
    }

    @PostMapping("/updatePromotion/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute("promotion") Promotion promotion, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "redirect:/updatePromotion/" + promotion.getId();
        }

        promotionService.updatePromotion(id, promotion);

        //Ajouter l'utilisateur créer au modèle pour l'affichage sur la page suivante
        model.addAttribute("promotion", promotion);
        return "redirect:../index";
    }


    @GetMapping("/delete/{id}")
    public String deletePromotion(@PathVariable Long id) {
        promotionService.deletePromotion(id);
        return "redirect:/promotions";
    }

    // Nouvelle méthode pour calculer le pourcentage en fonction du prix du produit
    @GetMapping("/calculatePercentage/{id}/{productPrice}")
    @ResponseBody
    public double calculatePercentage(@PathVariable Long id, @PathVariable double productPrice) {
        Promotion promotion = promotionService.getPromotionById(id);
        return promotionService.calculatePercentage(promotion, productPrice);
    }
}
