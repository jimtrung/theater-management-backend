import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.UUID;

public record PromotionResponse(
    UUID id,
    String name,
    @JsonProperty("start_date") OffsetDateTime startDate,
    @JsonProperty("end_date") OffsetDateTime endDate,
    String description,
    @JsonProperty("image_url") String imageUrl
) {}
