package com.github.jimtrung.theater.controller;

import com.github.jimtrung.theater.dto.PromotionRequest;
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

    // Lấy tất cả danh sách khuyến mãi
    @GetMapping
    public ResponseEntity<List<PromotionRequest>> getAllPromotions() {
        List<PromotionRequest> promotions = promotionService.getAllPromotionRequests();
        if (promotions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(promotions);
    }

    // Lấy danh sách các khuyến mãi đang diễn ra (Active)
    @GetMapping("/active")
    public ResponseEntity<List<PromotionRequest>> getActivePromotions() {
        List<PromotionRequest> activePromotions = promotionService.getActivePromotions();
        if (activePromotions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(activePromotions);
    }
}
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
        return ResponseEntity.ok(promotionService.getPromotionsWithImages());
    }
}
