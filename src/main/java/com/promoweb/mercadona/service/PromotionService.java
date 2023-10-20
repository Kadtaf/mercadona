package com.promoweb.mercadona.service;

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

    public void updatePromotion(Long id, Promotion promotion) {
        Promotion existingPromotion = getPromotionById(id);
        if (existingPromotion != null) {
            existingPromotion.setStartDate(promotion.getStartDate());
            existingPromotion.setEndDate(promotion.getEndDate());
            existingPromotion.setDiscountPercentage(promotion.getDiscountPercentage());

            // Utilisez la méthode save pour sauvegarder et récupérer la promotion mise à jour
             promotionRepository.save(existingPromotion);
        } else {
            throw new EntityNotFoundException("La promotion avec l'id : " + id + " n'existe pas");
        }

    }


    public void deletePromotion(Long id) {
        promotionRepository.deleteById(id);
    }

    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }

    public double calculatePercentage(Promotion promotion, double productPrice) {
        if (isValidPromotion(promotion) && productPrice > 0) {
            return (promotion.getDiscountPercentage() / 100) * productPrice;
        }
        return 0.0;
    }

    public Page<Promotion> findPromotionsWithPagination(String kw, Pageable pageable) {
        if (kw == null || kw.trim().isEmpty()) {
            // Si le mot-clé est vide, récupérez tous les utilisateurs avec pagination
            return promotionRepository.findAll(pageable);
        } else {
            // Si un mot-clé est fourni, recherchez les utilisateurs par mot-clé avec pagination
            return promotionRepository.findByStartDateContains(kw, pageable);
        }
    }

    public Promotion addPromotion(Promotion promotion) {
       if (isValidPromotion(promotion)) {

        return promotionRepository.save(promotion);
       }
       return null;
    }

    public boolean isValidPromotion(Promotion promotion) {
        return promotion != null
                && promotion.getStartDate() != null
                && promotion.getEndDate() != null
                && promotion.getDiscountPercentage() > 0
                && !promotion.getEndDate().isBefore(promotion.getStartDate());
    }

}
