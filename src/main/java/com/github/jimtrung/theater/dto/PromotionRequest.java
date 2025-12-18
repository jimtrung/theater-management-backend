package com.github.jimtrung.theater.dto;

import java.time.OffsetDateTime;

public record PromotionRequest(
        String name,
        OffsetDateTime description,
        OffsetDateTime startDate,
        String endDate
) {
}