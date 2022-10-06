package travel.domain;
import java.time.LocalDate;
import lombok.Data;

@Data
public class Visit {
    private long id;
    private LocalDate visitDate;
    private Attraction attraction;


}
