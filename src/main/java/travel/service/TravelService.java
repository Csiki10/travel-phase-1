package travel.service;

import travel.domain.*;

import java.time.LocalDate;
import java.util.List;

public interface TravelService {
    User authenticateUser(Credentials credentials);
    Statistics getStatistics();
    List<Destination> getDestinations();
    List<Review> getReviews(long attractionId);
    void createAttraction(long destinationId, Attraction attraction);
    List<Trip> getTrips(LocalDate startDate, LocalDate endDate);
    void createTrip(Trip trip);
}
