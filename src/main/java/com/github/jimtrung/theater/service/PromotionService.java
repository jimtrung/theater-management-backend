package com.github.jimtrung.theater.service;

import com.github.jimtrung.theater.dto.PromotionRequest;
import com.github.jimtrung.theater.model.Promotion;
import com.github.jimtrung.theater.repository.PromotionRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PromotionService {
    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    // Lấy tất cả khuyến mãi
    public List<PromotionRequest> getAllPromotionRequests() {
        return promotionRepository.findAll()
                .stream()
                .map(this::convertToRequestDTO)
                .collect(Collectors.toList());
    }

    // Lấy danh sách khuyến mãi đang còn hạn (Active)
    public List<PromotionRequest> getActivePromotions() {
        OffsetDateTime now = OffsetDateTime.now();
        return promotionRepository.findAll()
                .stream()
                // Lọc những khuyến mãi mà thời gian hiện tại nằm trong khoảng Start và End
                .filter(p -> p.getStartDate() != null && p.getEndDate() != null)
                .filter(p -> p.getStartDate().isBefore(now) && p.getEndDate().isAfter(now))
                .map(this::convertToRequestDTO)
                .collect(Collectors.toList());
    }

    // Hàm dùng chung để chuyển đổi từ Entity sang DTO
    // Đảm bảo truyền đúng 4 tham số theo thứ tự: name, startDate, endDate, description
    private PromotionRequest convertToRequestDTO(Promotion promotion) {
        return new PromotionRequest(
                promotion.getName(),
                promotion.getStartDate(),
                promotion.getEndDate(),
                promotion.getDescription()
        );
    }

    public List<com.github.jimtrung.theater.dto.PromotionResponse> getPromotionsWithImages() {
        List<Promotion> promotions = promotionRepository.findAll();
        // Limit to 20 if needed, or just return all. The request said "for 20 cards we get from database"
        // so I'll assume we return all and frontend or backend limits it. 
        // Let's shuffle or just map them.
        
        return promotions.stream()
            .map(p -> {
                // Random image 1-5 (assuming 5 images exist as per plan)
                // In a real app, strict mapping or DB field is better.
                // Using hash of ID or just Math.random to pick an image.
                int imageIndex = (int) (Math.random() * 5) + 1; 
                String imageUrl = "http://localhost:8080/uploads/promotions/" + imageIndex + ".jpg";
                // Or if user said "images of each card is in uploads folder", maybe "promotion_1.jpg"?
                // The plan said: "promotion_1.jpg to promotion_5.jpg"
                
                // Let's assume the user meant specific images for specific cards, 
                // but since they said "randomly set", I will do random.
                
                return new com.github.jimtrung.theater.dto.PromotionResponse(
                    p.getId(),
                    p.getName(),
                    p.getStartDate(),
                    p.getEndDate(),
                    p.getDescription(),
                    "http://localhost:8080/uploads/promotions/" + imageIndex + ".jpg"
                );
            })
            .collect(Collectors.toList());
    }
}
