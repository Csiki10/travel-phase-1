package travel.domain;

import java.time.LocalDate;
import java.util.List;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public void setVisits(List<Visit> visits) {
        this.visits = visits;
    }
}
