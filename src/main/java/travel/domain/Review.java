package travel.domain;
import lombok.Data;

@Data
public class Review {

    private long id;
    private int rating;
    private String comment;
    private Attraction attraction;
    private User user;
}
