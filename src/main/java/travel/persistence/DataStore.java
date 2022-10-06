package travel.persistence;

import travel.domain.*;

import java.util.List;

public interface DataStore {
    void loadData();
    void saveData();
    List<Destination> getDestinations();
    List<User> getUsers();
    List<Trip> getTrips();
    List<Review> getReviews();
    List<Attraction> getAttractions();

}
