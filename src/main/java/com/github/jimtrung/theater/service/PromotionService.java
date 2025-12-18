package com.github.jimtrung.theater.service;

import com.github.jimtrung.theater.dto.PromotionResponse;
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

    public List<PromotionResponse> getActivePromotionsWithImages() {
        OffsetDateTime now = OffsetDateTime.now();
        return promotionRepository.findAll().stream()
                .filter(p -> p.getStartDate() != null && p.getEndDate() != null)
                .filter(p -> p.getStartDate().isBefore(now) && p.getEndDate().isAfter(now))
                .map(this::mapToResponseWithImage)
                .collect(Collectors.toList());
    }

    private PromotionResponse mapToResponseWithImage(Promotion p) {
        return new PromotionResponse(
                p.getId(),
                p.getName(),
                p.getStartDate(),
                p.getEndDate(),
                p.getDescription(),
                p.getImageUrl()
        );
    }

    public List<PromotionResponse> getPromotionsWithImages() {
        return promotionRepository.findAll().stream()
                .map(this::mapToResponseWithImage)
                .collect(Collectors.toList());
    }
}
