package travel.persistence;

import travel.domain.Destination;
import travel.domain.Review;
import travel.domain.Trip;
import travel.domain.User;

import java.util.List;

public interface DataStore {
    void loadData();
    void saveData();
    List<Destination> getDestinations();
    List<User> getUsers();
    List<Trip> getTrips();
    List<Review> getReviews();
}
