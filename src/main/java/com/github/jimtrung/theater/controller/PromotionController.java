package com.github.jimtrung.theater.controller;

import com.github.jimtrung.theater.dto.PromotionResponse;
import com.github.jimtrung.theater.service.PromotionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/promotions")
public class PromotionController {

    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping
    public ResponseEntity<List<PromotionResponse>> getAllPromotions() {
        List<PromotionResponse> promotions = promotionService.getPromotionsWithImages();
        if (promotions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(promotions);
    }

    @GetMapping("/active")
    public ResponseEntity<List<PromotionResponse>> getActivePromotions() {
        List<PromotionResponse> activePromotions = promotionService.getActivePromotionsWithImages();
        if (activePromotions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(activePromotions);
    }
}
