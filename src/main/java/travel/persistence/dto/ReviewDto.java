package travel.persistence.dto;

import lombok.Data;

@Data
public class ReviewDto {
    private long id;
    private int userId;
    private int rating;
    private String comment;
    private long attractionId;
}

