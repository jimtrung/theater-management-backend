package com.github.jimtrung.theater.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record PromotionResponse(
    UUID id,
    String name,
    OffsetDateTime startDate,
    OffsetDateTime endDate,
    String description,
    String imageUrl
) {}
