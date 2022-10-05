package travel.domain;

import java.time.LocalDate;

public class Visit {

    private long id;
    private LocalDate visitDate;
    private Attraction attraction;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public void setAttraction(Attraction attraction) {
        this.attraction = attraction;
    }

    public Attraction getAttraction() {
        return attraction;
    }
}
