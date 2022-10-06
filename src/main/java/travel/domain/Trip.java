package travel.domain;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;
@Data
public class Trip {

    private long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private User user;
    private Destination destination;
    private List<Visit> visits;

    public Trip(long id, LocalDate startDate, LocalDate endDate, User user, Destination destination, List<Visit> visits) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
        this.destination = destination;
        this.visits = visits;
    }

    public Trip() {
    }
}
