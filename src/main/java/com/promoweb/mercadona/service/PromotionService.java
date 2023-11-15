package com.promoweb.mercadona.service;

import com.promoweb.mercadona.model.Category;
import com.promoweb.mercadona.model.Product;
import com.promoweb.mercadona.model.Promotion;
import com.promoweb.mercadona.repository.PromotionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public Promotion getPromotionById(Long id) {
        return promotionRepository.findById(id).orElse(null);
    }

    public void updatePromotion(Promotion promotion) {

        promotionRepository.save(promotion);
    }


    public void deletePromotion(Long id, Promotion promotion) throws Exception {

        List<Product> products = promotion.getProducts();
        if (products != null && !products.isEmpty()) {
            throw new Exception("Cette promotion est lié à des produits, elle ne peut pas être supprimé");
        } else {
            promotionRepository.deleteById(id);
        }

    }


    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }

    public Page<Promotion> findPromotionsWithPagination(String kw, Pageable pageable) {
        if (kw == null || kw.trim().isEmpty()) {

            return promotionRepository.findAll(pageable);
        } else {

            return promotionRepository.findByDiscountPercentageContains(kw, pageable);
        }
    }

    public void addPromotion(Promotion promotion) {
       if (isValidPromotion(promotion)) {

           promotionRepository.save(promotion);
       }
    }

    public boolean isValidPromotion(Promotion promotion) {
        return promotion != null
                && promotion.getStartDate() != null
                && promotion.getEndDate() != null
                && promotion.getDiscountPercentage() > 0
                && !promotion.getEndDate().isBefore(promotion.getStartDate());
    }

    public Promotion createPromotion(Promotion promotion) {
        // Validation des champs obligatoires
        if (isValidPromotion(promotion) && isValidDateRange(promotion)) {
            // Enregistrement dans la base de données
            return promotionRepository.save(promotion);
        }
        return null;
    }

    // Validation de la période de validité
    private boolean isValidDateRange(Promotion promotion) {
        return promotion.getStartDate() != null && promotion.getEndDate() != null
                && !promotion.getEndDate().isBefore(promotion.getStartDate());
    }
}
