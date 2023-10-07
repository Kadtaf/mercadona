package com.promoweb.mercadona.controller;


import com.promoweb.mercadona.model.Promotion;
import com.promoweb.mercadona.service.PromotionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {

    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }


    // Read
    @GetMapping("/{id}")
    public ResponseEntity<Promotion> getPromotionById(@PathVariable Long id) {
        Promotion promotion = promotionService.getPromotionById(id);
        if (promotion != null) {
            return  ResponseEntity.ok(promotion);
        } else {
            throw new EntityNotFoundException("La promotion avec l'id : " + id + " n'existe pas");
        }
    }

    // Create
    @PostMapping
    public ResponseEntity<Promotion> createPromotion(@RequestBody Promotion promotion) {
        Promotion createdPromotion = promotionService.createPromotion(promotion);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPromotion);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Promotion> updatePromotion(@PathVariable Long id, @RequestBody Promotion promotion) {
        Promotion updatedPromotion = promotionService.updatePromotion(id, promotion);
        if (updatedPromotion !=null) {
            return ResponseEntity.ok(updatedPromotion);
        } else {
            throw new EntityNotFoundException("La promotion avec l'id : " + id + " n'existe pas");
        }

    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromotion(@PathVariable Long id) {
        promotionService.deletePromotion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Promotion>> getAllPromotions() {
        List<Promotion> promotions = promotionService.getAllPromotions();
        return ResponseEntity.ok(promotions);
    }

}
