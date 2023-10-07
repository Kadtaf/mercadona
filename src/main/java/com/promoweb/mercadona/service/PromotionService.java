package com.promoweb.mercadona.service;


import com.promoweb.mercadona.model.Product;
import com.promoweb.mercadona.model.Promotion;
import com.promoweb.mercadona.repository.ProductRepository;
import com.promoweb.mercadona.repository.PromotionRepository;
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

    public Promotion createPromotion(Promotion promotion) {
        //Enregistrement dans la base de données
        if (isValidPromotion(promotion)) {
            return promotionRepository.save(promotion);
        }
        return null;
    }

    public Promotion updatePromotion(Long id, Promotion promotion) {
        Promotion existingPromotion = getPromotionById(id);
        if (existingPromotion != null) {
            //Enregistrement dans la base de données
            existingPromotion.setStartDate(promotion.getStartDate());
            existingPromotion.setEndDate(promotion.getEndDate());
            existingPromotion.setDiscountPercentage(promotion.getDiscountPercentage());
            return promotionRepository.save(existingPromotion);
        }
        return null;
    }

    public void deletePromotion(Long id) {
        promotionRepository.deleteById(id);
    }

    private boolean isValidPromotion(Promotion promotion) {

        return promotion != null && promotion.getDiscountPercentage() > 0 ;
    }

    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }

}
